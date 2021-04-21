package vip.bingzi.randomentrypro.io

import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.module.nms.nbt.NBTBase
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.item.inventory.MenuBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import vip.bingzi.randomentrypro.RandomEntryPro
import vip.bingzi.randomentrypro.util.REPlayerPoints
import vip.bingzi.randomentrypro.util.REUtil.getNBTCompound
import vip.bingzi.randomentrypro.util.REUtil.logger
import vip.bingzi.randomentrypro.util.REUtil.randomValue
import vip.bingzi.randomentrypro.util.REVault
import java.io.File
import kotlin.system.measureTimeMillis

object REMenu {
    /**
     * 菜单文件检测
     * @param plugin 执行的插件
     * @param path 指向的目录而不是一个具体的文件
     */
    fun menuInspection(plugin: Plugin, path: String) {
        val file = File(path)
        val resourceFileList: ArrayList<String> = arrayListOf("main.yml", "confirm.yml", "cancel.yml")
        if (file.exists()) {
            logger.info("菜单目录已存在。")
        } else {
            logger.warn("菜单目录不存在，已创建")
            file.mkdirs()
        }
        val list = file.list()
        if (list != null) {
            resourceFileList.removeAll(list)
        }
        logger.info("等待释放的菜单数量为:${resourceFileList.size}")
        for (s in resourceFileList) {
            plugin.saveResource("gui/$s", false)
            logger.info("> $s 已释放")
        }
    }

    /**
     * 进行界面输出
     * @param plugin 插件
     * @param MenuName 菜单名
     * @param player 执行的玩家
     */
    fun menuOut(plugin: Plugin, MenuName: String, player: Player) {
        val config = TConfig.create(File("${plugin.dataFolder}/gui/${MenuName}.yml"))
        MenuBuilder.builder().also {
            it.title(config.getStringColored("Title"))
            it.rows(config.getStringListColored("Shape").size)
            it.items(*config.getStringList("Shape").toTypedArray())
            for (key in config.getConfigurationSection("Buttons")!!.getKeys(false)) {
                try {
                    if (key == " ") continue
                    val itemBuilder =
                        ItemBuilder(Material.matchMaterial(config.getString("Buttons.${key}.display.mats").toString()))
                    itemBuilder.lore(config.getStringListColored("Buttons.${key}.display.lore"))
                    itemBuilder.name(config.getStringColored("Buttons.${key}.display.name"))
                    for (s in config.getStringList("Buttons.${key}.display.flag")) {
                        itemBuilder.flags(ItemFlag.valueOf(s))
                    }
                    if (config.getBoolean("Buttons.${key}.display.shiny")) {
                        itemBuilder.shiny()
                    }
                    val build = itemBuilder.build()
                    getNBTCompound(build).also { nbtCompound ->
                        nbtCompound["Menu"] = NBTBase.toNBT("true")
                        nbtCompound.saveTo(build)
                    }
                    it.put(key.toCharArray()[0], build)
                } catch (e: NullPointerException) {
                    logger.warn("请注意！在构建菜单的时候发生了异常，请检查GUI文件物品ID是否符合当前版本需求。默认提供的版本为1.13及其以上适用的ID，如果使用1.12及其以下版本请更换物品ID。")
                    return
                }
            }
            it.event { clickEvent ->
                when (clickEvent.slot) {
                    ' ' -> {
                        logger.verbose("${clickEvent.clicker.name}的点击被更改为了点击事件。")
                    }
                    'A' -> {
                        logger.verbose("${clickEvent.clicker.name}的点击被更改金币鉴定事件。")
                        clickEvent.isCancelled = true
                        val appraisalScope = appraisalScope(clickEvent.inventory)
                        if (appraisalScope.size == 0) {
                            logger.fine("没有待鉴定的物品。")
                            return@event
                        }
                        val measureTimeMillis = measureTimeMillis {
                            determine(appraisalScope, clickEvent.inventory, "vaultidentify", player)
                        }
                        logger.fine("金币鉴定耗时${measureTimeMillis}")

                    }
                    'B' -> {
                        logger.verbose("${clickEvent.clicker.name}的点击被更改取消事件。")
                        clickEvent.isCancelled = true
                        val appraisalScope = appraisalScope(clickEvent.inventory)
                        if (appraisalScope.size == 0) {
                            logger.fine("没有待退还的物品。")
                            return@event
                        }
                        cancel(appraisalScope, clickEvent.inventory, player)
                    }
                    'C' -> {
                        logger.verbose("${clickEvent.clicker.name}的点击被更改点券鉴定事件。")
                        clickEvent.isCancelled = true
                        val appraisalScope = appraisalScope(clickEvent.inventory)
                        if (appraisalScope.size == 0) {
                            logger.fine("没有待鉴定的物品。")
                            return@event
                        }
                        val measureTimeMillis = measureTimeMillis {
                            determine(appraisalScope, clickEvent.inventory, "pointsidentify", player)
                        }
                        logger.fine("点券鉴定耗时${measureTimeMillis}")
                    }
                    else -> {
                        logger.verbose("${clickEvent.clicker.name}的点击被取消。")
                        clickEvent.isCancelled = true
                    }
                }
            }
            it.close { inventoryCloseEvent ->
                val appraisalScope = appraisalScope(inventoryCloseEvent.inventory)
                if (appraisalScope.size == 0) {
                    logger.fine("没有待退还的物品。")
                    return@close
                }
                cancel(appraisalScope, inventoryCloseEvent.inventory, player)
            }
        }.open(player)
    }

    /**
     * 检测界面中有多少等待检测的物品
     */
    private fun appraisalScope(inventory: Inventory): ArrayList<Int> {
        logger.verbose("界面大小为${inventory.size}")
        val list = arrayListOf<Int>()
        for ((i, itemStack) in inventory.storageContents.withIndex()) {
            try {
                logger.verbose("第${i + 1}个物品名称为${itemStack.itemMeta?.displayName}")
                getNBTCompound(itemStack).also {
                    if (it["Menu"].toString() != "true") {
                        logger.verbose("已经第${i + 1}个物品加入玩家物品列表中")
                        list.add(i)
                    }
                }
            } catch (e: Exception) {
                logger.verbose("第${i + 1}个物品为空")
            }
        }
        logger.verbose("玩家的物品位置：${list}")
        return list
    }

    /**
     * 实际鉴定流程
     * @param list 要鉴定的物品位置
     * @param inventory 要鉴定的页面
     * @param info 以什么方式进行鉴定
     * @param player 玩家
     */
    private fun determine(list: ArrayList<Int>, inventory: Inventory, info: String, player: Player) {
        val config: TConfig = when (info) {
            "vaultidentify" -> {
                RandomEntryPro.vaultidentify
            }
            "pointsidentify" -> {
                RandomEntryPro.pointsidentify
            }
            else -> {
                logger.warn("未指定菜单名，无法进行鉴定操作！")
                return
            }
        }
        val identifyLoreToRandomList = hashMapOf<String, List<String>>()
        val identifyLoreToNode = hashMapOf<String, String>()
        for (s in config.getKeys(false)) {
            val string = config.getStringColored("${s}.IdentifyLore")
            identifyLoreToNode[string] = s
            identifyLoreToRandomList[string] = config.getStringListColored("${s}.RandomList")
        }
        // 成功个数
        var success = 0
        // 失败个数
        var failure = 0
        // 未找到个数
        var notFound = 0
        for (i in list) {
            logger.finest("正在鉴定${i + 1}处位置的物品")
            val item = inventory.getItem(i)
            val itemMeta = item!!.itemMeta
            if (itemMeta != null) {
                val lore = itemMeta.lore
                if (lore != null) {
                    for (key in identifyLoreToRandomList.keys) {
                        if (lore.indexOf(key) != -1) {
                            when (info) {
                                "vaultidentify" -> {
                                    val bankWithdraw = REVault.getEconomy()
                                        .withdrawPlayer(player,
                                            config.getDouble("${identifyLoreToNode[key]}.Spend"))
                                    logger.verbose("金币扣除结果：${bankWithdraw.transactionSuccess()}，返回类型：${bankWithdraw.type}，扣除金额：${
                                        config.getDouble("${identifyLoreToNode[key]}.Spend")
                                    }")
                                    if (!bankWithdraw.transactionSuccess()) {
                                        failure += 1
                                        continue
                                    }
                                }
                                "pointsidentify" -> {
                                    val take = REPlayerPoints.getPlayerPointsAPI()
                                        .take(player.uniqueId, config.getInt("${identifyLoreToNode[key]}.Spend"))
                                    logger.verbose("点券扣除结果：${take}")
                                    if (!take) {
                                        failure += 1
                                        continue
                                    }
                                }
                            }
                            lore[lore.indexOf(key)] = randomValue(identifyLoreToRandomList[key]!!.random())
                            itemMeta.lore = lore
                            item.itemMeta = itemMeta
                            inventory.setItem(i, item)
                            success += 1
                        } else {
                            notFound += 1
                        }
                    }
                    logger.fine("已鉴定${i + 1}处位置的物品")
                } else {
                    logger.fine("${i + 1}处位置的物品缺少必要的Lore，不支持鉴定。")
                }
            } else {
                logger.fine("${i + 1}处位置的物品没有Lore，不支持鉴定。")
            }

        }
        player.sendMessage(TLocale.asString("Identify.Complete").format(success, failure, notFound, list.size))
    }

    /**
     * 将列表中的物品都进行返还
     */
    private fun cancel(list: ArrayList<Int>, inventory: Inventory, player: Player) {
        val playerInventory = player.inventory
        for (i in list) {
            val item = inventory.getItem(i)
            inventory.setItem(i, ItemStack(Material.AIR))
            if (playerInventory.firstEmpty() != -1) {
                playerInventory.setItem(playerInventory.firstEmpty(), item)
            } else {
//                logger.warn("玩家${player.name}已满，已经转存到玩家临时仓库中。节点：${player.name}_${random}.${item!!.type}")
//                RandomEntryPro.warehouse.set("${player.name}.${item.type}",item)
//                RandomEntryPro.warehouse.saveToFile()
                player.world.dropItem(player.location, item!!)
            }
        }
    }
}
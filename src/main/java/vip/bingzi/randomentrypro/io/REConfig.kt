package vip.bingzi.randomentrypro.io

import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.util.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import vip.bingzi.randomentrypro.RandomEntryPro
import vip.bingzi.randomentrypro.listener.REUtil.logger
import java.io.File

class REConfig(configName: String) {
    private val config = TConfig.create(File("${RandomEntryPro.plugin.dataFolder}\\gui\\${configName}"))

    /**
     * 获取标题
     */
    fun getTitle(): String {
        return config.getString("Title").toString()
    }

    /**
     * 获取形状
     */
    fun getShape(): MutableList<String> {
        return config.getStringList("Shape")
    }

    /**
     * 获取按钮节点
     */
    fun getButtons(): MutableMap<Char, ItemStack> {
        val mutableMap: MutableMap<Char, ItemStack> = mutableMapOf()
        val keys = config.getConfigurationSection("Buttons")?.getKeys(false)
        if (keys != null) {
            for (k in keys) {
                logger.finest("正在进行节点 $k 物品构建，名称为: ${config.getString("Buttons.${k}.display.name")}")
                // 以给定ID构建物品
                val itemBuilder =
                    ItemBuilder(Material.matchMaterial(config.getString("Buttons.${k}.display.mats").toString()))
                // 给物品添加名字
                itemBuilder.name(config.getString("Buttons.${k}.display.name"))
                // 给物品添加描述
                itemBuilder.lore(config.getStringList("Buttons.${k}.display.lore"))
                // 给物品添加隐藏信息
                for (flags in config.getStringList("Buttons.${k}.display.flag")) {
                    itemBuilder.flags(ItemFlag.valueOf(flags))
                }
                // 将物品写进Map中
                mutableMap[k.first()] = itemBuilder.build()
            }
        }
        return mutableMap
    }
}
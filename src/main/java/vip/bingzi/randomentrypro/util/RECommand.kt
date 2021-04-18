package vip.bingzi.randomentrypro.util

import io.izzel.taboolib.module.command.base.*
import io.izzel.taboolib.module.locale.logger.TLogger
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.bingzi.randomentrypro.RandomEntryPro
import vip.bingzi.randomentrypro.io.REMenu.menuOut
import vip.bingzi.randomentrypro.util.REUtil.logger

@BaseCommand(name = "RandomEntryPro",
    aliases = ["randomentrypro", "rep", "randomentry"],
    permission = "randomentrypro.use")
class RECommand : BaseMainCommand() {
    @SubCommand
    var open: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "打开鉴定菜单"
        }

        override fun getPermission(): String {
            return "randomentrypro.use"
        }

        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
            if (sender is Player) {
                menuOut(RandomEntryPro.plugin, "main", sender)
            }
        }
    }

    /*
    @SubCommand
    var draw: BaseSubCommand = object : BaseSubCommand() {
    override fun getDescription(): String {
    return "提取临时储存在仓库的物品"
    }

    override fun getPermission(): String {
    return "randomentrypro.use"
    }

    override fun getArguments(): Array<Argument> {
    return arrayOf(
    Argument("临时仓库名称")
    )
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
    if (sender is Player) {
    val list = arrayListOf<String>()
    for (key in warehouse.getKeys(false)) {
    if (key.contains(sender.name, true)) {
    list.add(key.replace("${sender.name}_", ""))
    }
    }
    if (args.isEmpty()) {
    sender.sendMessage(TLocale.asString("Draw.ExistingWarehouse"))
    for (s in list) {
    sender.sendMessage(TLocale.asString("Draw.WarehouseList", s))
    }
    } else {
    if (list.contains("${sender.name}_${args[0]}")) {
    val keys =
    warehouse.getConfigurationSection("${sender.name}_${args[0]}.item")!!.getKeys(false)
    val itemList = arrayListOf<ItemStack>()
    for (key in keys) {
    itemList.add(warehouse.get("${sender.name}_${args[0]}.item.${keys}") as ItemStack)
    }
    warehouse.set("${sender.name}_${args[0]}",null)

    } else {
    sender.sendMessage(TLocale.asString("Draw.NoSuchWarehouse"))
    }
    }
    }
    }
    }
    */

    @SubCommand
    var adminUtil: BaseSubCommand = object : BaseSubCommand() {
        override fun getPermission(): String {
            return "randomentrypro.admin"
        }

        override fun getDescription(): String {
            return "管理员工具"
        }

        override fun getArguments(): Array<Argument> {
            return arrayOf(
                Argument("debug(调试)/logger(日志-需要参数等级)/info(信息)") {
                    listOf("debug",
                        "logger",
                        "info")
                },
                Argument("等级(默认为INFO)", false) { listOf("VERBOSE", "FINEST", "FINE", "INFO", "WARN", "ERROR", "FATAL") }
            )
        }

        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
            when (args[0]) {
                "debug" -> {
                    // 设置日志输出等级
                    logger.level = TLogger.FINEST
                }
                "logger" -> {
                    // 设置日志输出等级
                    logger.level = when (args[1]) {
                        "VERBOSE" -> TLogger.VERBOSE
                        "FINEST" -> TLogger.FINEST
                        "FINE" -> TLogger.FINE
                        "INFO" -> TLogger.INFO
                        "WARN" -> TLogger.WARN
                        "ERROR" -> TLogger.ERROR
                        "FATAL" -> TLogger.FATAL
                        else -> TLogger.INFO
                    }
                }
                "info" -> {
                    logger.info("Logger Level: ${logger.level}")
                }
                else -> {
                    logger.warn("${sender.name}的管理员命令执行失败，请检查命令格式。")
                }
            }
        }
    }
}
package vip.bingzi.randomentrypro.util

import io.izzel.taboolib.module.locale.logger.TLogger
import io.izzel.taboolib.module.locale.logger.TLoggerManager
import io.izzel.taboolib.module.nms.NMS
import io.izzel.taboolib.module.nms.nbt.NBTCompound
import org.bukkit.inventory.ItemStack
import vip.bingzi.randomentrypro.RandomEntryPro

object REUtil {
    // 日志系统
    val logger: TLogger = TLoggerManager.getLogger(RandomEntryPro.plugin)

    /**
     * 提取物品NBT
     */
    fun getNBTCompound(itemStack: ItemStack): NBTCompound {
        return NMS.handle().loadNBT(itemStack)
    }

    fun randomValue(string: String): String {
        val replaceString = hashMapOf<String, String>()
        var temp = string
        val regex = Regex(pattern = """\d+.-.\d+""")
        for ((i, matchResult) in regex.findAll(string).withIndex()) {
            logger.verbose("匹配的第 $i 个为: ${matchResult.value}")
            replaceString[matchResult.value] = randomRange(matchResult.value)
        }
        for (key in replaceString.keys) {
            temp = temp.replace(key, replaceString[key].toString())
        }
        temp = temp.replace("|", "")
        logger.verbose("返回的字符串为：$temp")
        return temp
    }

    private fun randomRange(string: String): String {
        val split = string.split("-")
        if (split.size == 2) {
            val toString = (split[0].toInt()..split[1].toInt()).random().toString()
            logger.verbose("随机后的结果为：$toString")
            return toString
        }
        return string
    }
}
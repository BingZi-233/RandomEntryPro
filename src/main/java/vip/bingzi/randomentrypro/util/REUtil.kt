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
}
package vip.bingzi.randomentrypro.listener

import io.izzel.taboolib.util.item.inventory.MenuBuilder
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import vip.bingzi.randomentrypro.RandomEntryPro

class REView {
    private val guiView: MenuBuilder = MenuBuilder.builder(RandomEntryPro.plugin)

    /**
     * 设置GUI标题
     */
    fun setGuiViewTitle(string: String) {
        guiView.title(string)
    }

    /**
     * 设置GUI行数
     */
    fun setGuiViewRows(int: Int) {
        guiView.rows(int)
    }

    /**
     * 打开GUI
     */
    fun openGuiView(player: Player) {
        guiView.open(player)
    }

    /**
     * 设置界面锁
     */
    fun lockGuiView() {
        guiView.lockHand()
    }

    /**
     * 设置节点物品
     */
    fun putGuiViewItem(key: Char, itemStack: ItemStack) {
        guiView.put(key, itemStack)
    }

    /**
     * 设置形状
     */
    fun itemGuiView(mutableSet: MutableSet<String>): Boolean {
        if (mutableSet.size == 0) return false
        guiView.items(*mutableSet.toTypedArray())
        return true
    }

    /**
     * 是否已经锁定
     */
    fun isLockGuiView(): Boolean {
        return guiView.isLockHand
    }
}
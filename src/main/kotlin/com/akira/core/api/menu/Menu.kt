package com.akira.core.api.menu

import com.akira.core.api.util.general.outOfBounds
import com.akira.core.api.util.general.unsprtOpera
import com.akira.core.api.util.text.toComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.*

/**
 * 菜单声明蓝图
 *
 * - 实现 [InventoryHolder] 以注入 [Inventory] 实例
 * - 禁用 [getInventory] 改用 [createInventory] 创建
 * - 注意：只声明结构与行为，不持有实例，不储存状态
 *
 * @property rows 菜单行数，范围 `1-6`
 * @property size 菜单格子数，对应 [Inventory.size]
 * @property layout 菜单布局预设
 */
abstract class Menu(val rows: Int) : InventoryHolder {
    private val layout: MutableMap<Int, MenuItem> = HashMap()
    val size = rows * 9

    init {
        require(rows > 0 && rows <= 6) {
            "Menu rows must be from 1 to 6, but actual: $rows."
        }
    }

    /**
     * 菜单布局只读视图。
     */
    val layoutView: Map<Int, MenuItem> get() = Collections.unmodifiableMap(layout)

    /**
     * 分发点击事件到对应 [slot] 的 [MenuItem] 中。
     * - 若 [slot] 不合法则抛出异常
     * - 若该槽位没有物品，则不作处理
     *
     * @throws IndexOutOfBoundsException 当 [slot] 越界 `[0,size)` 时抛出
     */
    fun click(player: Player, slot: Int) {
        layout[slot.validateSlot()]?.click(player)
    }

    /**
     * 判断 [slot] 对应的槽位是否有物品。
     */
    fun hasItem(slot: Int): Boolean {
        return layout.containsKey(slot.validateSlot())
    }

    /**
     * 获取 [slot] 对应物品，若不存在则返回 `null`。
     */
    fun getItem(slot: Int): MenuItem? {
        return layout[slot.validateSlot()]
    }

    /**
     * 把 [slot] 设置为 [item]，将覆盖已有物品。
     */
    fun setItem(slot: Int, item: MenuItem) {
        layout.put(slot.validateSlot(), item)
    }

    /**
     * 为 [player] 创建一个专属 [Inventory] 后打开。
     */
    fun open(player: Player) {
        player.openInventory(createInventory(player))
    }

    /**
     * 为 [player] 创建一个专属 [Inventory]。
     */
    fun createInventory(player: Player): Inventory {
        val title = this.generateTitle(player).toComponent()
        val inv = Bukkit.createInventory(this, size, title)

        layout.forEach { (slot, item) -> inv.setItem(slot, item.create(player)) }
        return inv
    }

    /**
     * 判断 [inventory] 是否属于该 [Menu]。
     */
    fun matches(inventory: Inventory) = inventory.holder == this

    /**
     * 为 [player] 生成专属标题。
     */
    abstract fun generateTitle(player: Player): String

    /**
     * 为实现创建行为统一，请改用 [createInventory]。
     */
    final override fun getInventory(): Inventory {
        unsprtOpera("Cannot get an inventory from a Menu. Use 'createInventory' instead.")
    }

    private fun Int.validateSlot(): Int {
        if (this >= 0 && this < size) return this
        else outOfBounds("Slot $this is out of range. Menu size: $size.")
    }
}
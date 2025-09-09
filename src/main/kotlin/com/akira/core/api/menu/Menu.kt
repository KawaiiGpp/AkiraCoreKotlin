package com.akira.core.api.menu

import com.akira.core.api.util.text.toComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

abstract class Menu(val rows: Int) : InventoryHolder {
    val size = rows * 9

    private val container: MutableMap<Int, MenuItem> = HashMap()
    val itemMap get() = container.toMap()

    init {
        require(rows > 0 && rows <= 6) { "Menu rows must be 1 ~ 6: $rows" }
    }

    fun click(player: Player, slot: Int) = itemMap[slot]?.click(player)

    fun hasItem(slot: Int): Boolean = container.containsKey(ensureSlot(slot))

    fun getItem(slot: Int): MenuItem? = container[ensureSlot(slot)]

    fun setItem(slot: Int, item: MenuItem) = container.put(ensureSlot(slot), item)

    fun open(player: Player) = player.openInventory(createInventory(player))

    fun createInventory(player: Player): Inventory {
        val title = this.generateTitle(player).toComponent()
        val inv = Bukkit.createInventory(this, size, title)

        container.forEach { (slot, item) -> inv.setItem(slot, item.create(player)) }
        return inv
    }

    fun matches(inventory: Inventory) = inventory.holder == this

    abstract fun generateTitle(player: Player): String

    final override fun getInventory(): Inventory =
        throw UnsupportedOperationException("Cannot get an inventory from Menu.")

    private fun ensureSlot(slot: Int): Int {
        require(slot >= 0 && slot < size) { "Illegal slot $slot. (Size=$size)" }
        return slot
    }
}
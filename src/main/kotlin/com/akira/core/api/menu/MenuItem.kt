package com.akira.core.api.menu

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class MenuItem(private val item: ItemStack) {
    fun copyItem() : ItemStack = item.clone()

    open fun create(player: Player): ItemStack = copyItem()

    open fun click(clicker: Player) {}
}
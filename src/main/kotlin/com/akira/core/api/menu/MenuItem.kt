package com.akira.core.api.menu

import com.akira.core.api.util.item.requireValidMeta
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class MenuItem(private val item: ItemStack) {
    init {
        requireValidMeta(item)
    }

    fun copyItem() : ItemStack = item.clone()

    open fun create(player: Player): ItemStack = copyItem()

    open fun click(clicker: Player) {}
}
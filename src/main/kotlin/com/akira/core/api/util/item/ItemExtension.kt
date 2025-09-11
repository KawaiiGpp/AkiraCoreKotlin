package com.akira.core.api.util.item

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

fun ItemStack.editMeta(block: ItemMeta.() -> Unit) {
    val meta = requireValidMeta(this)
    meta.block()
    itemMeta = meta
}
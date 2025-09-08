package com.akira.core.api.util.item

import org.bukkit.inventory.ItemStack

fun createItem(block: ItemBuilder.() -> Unit): ItemStack {
    val builder = ItemBuilder()
    builder.block()
    return builder.build()
}
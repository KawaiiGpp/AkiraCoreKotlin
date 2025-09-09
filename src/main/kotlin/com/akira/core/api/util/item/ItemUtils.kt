package com.akira.core.api.util.item

import com.akira.core.api.AkiraPlugin
import org.bukkit.inventory.ItemStack

fun createItem(block: ItemBuilder.() -> Unit): ItemStack =
    ItemBuilder().apply(block).build()

fun editItemTag(plugin: AkiraPlugin, item: ItemStack, block: ItemTagEditor.() -> Unit)=
    ItemTagEditor.forItemMeta(plugin, item).apply(block).apply(item)

fun ensureLegitItem(item: ItemStack?) {
    requireNotNull(item) { "Item is null." }
    require(item.type.isItem) { "Material ${item.type} is not an item." }
    requireNotNull(item.itemMeta) { "Item (type=${item.type}) doesn't have item meta." }
}
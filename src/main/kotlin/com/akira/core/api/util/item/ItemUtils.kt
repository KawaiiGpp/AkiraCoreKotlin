package com.akira.core.api.util.item

import com.akira.core.api.AkiraPlugin
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

fun createItem(block: ItemBuilder.() -> Unit): ItemStack =
    ItemBuilder().apply(block).build()

fun editItemTag(plugin: AkiraPlugin, item: ItemStack, block: ItemTagEditor.() -> Unit) =
    ItemTagEditor.forItemMeta(plugin, item).apply(block).apply(item)

fun requireValidMeta(item: ItemStack?): ItemMeta {
    requireNotNull(item) { "Item is null." }
    require(item.type.isItem) { "Material ${item.type} is not an item." }

    return requireNotNull(item.itemMeta) { "Item (type=${item.type}) doesn't have item meta." }
}
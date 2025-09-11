package com.akira.core.api.util.item

import com.akira.core.api.AkiraPlugin
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

class ItemTagEditor(
    private val plugin: AkiraPlugin,
    private val meta: ItemMeta
) {
    private val data get() = meta.persistentDataContainer

    fun <T : Any> get(key: String, type: PersistentDataType<*, T>): T? =
        data.get(createKey(key), type)

    fun <T : Any> get(key: String, type: PersistentDataType<*, T>, default: T): T =
        data.getOrDefault(createKey(key), type, default)

    fun <T : Any> set(key: String, type: PersistentDataType<*, T>, value: T) =
        data.set(createKey(key), type, value)

    fun <T : Any> has(key: String, type: PersistentDataType<*, T>): Boolean =
        data.has(createKey(key), type)

    fun remove(key: String) = data.remove(createKey(key))

    fun apply(item: ItemStack) {
        requireValidMeta(item)
        item.itemMeta = meta.clone()
    }

    private fun createKey(key: String): NamespacedKey = NamespacedKey(plugin, key)

    companion object {
        fun forItemMeta(plugin: AkiraPlugin, item: ItemStack): ItemTagEditor =
            ItemTagEditor(plugin, requireValidMeta(item))
    }
}
package com.akira.core.api.util.item

import com.akira.core.api.AkiraPlugin
import com.akira.core.api.util.general.illegalArg
import com.akira.core.api.util.general.noSuchElm
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

/**
 * 物品标签编辑器
 *
 * - 封装对 [PersistentDataType] 的操作
 * - 自动根据 [plugin] 生成 [NamespacedKey]
 *
 * @property plugin 所属插件
 * @property meta 编辑对象
 */
class ItemTagEditor(
    private val plugin: AkiraPlugin,
    private val meta: ItemMeta
) {
    private val data get() = meta.persistentDataContainer

    /**
     * 为 [ItemStack] 创建物品标签编辑器
     *
     * - 自动验证 [ItemStack.itemMeta] 并将其作为编辑对象。
     *
     * @throws IllegalArgumentException 当物品实例不适用
     */
    constructor(plugin: AkiraPlugin, item: ItemStack) : this(plugin, item.requiredMeta)

    /**
     * 通过 [key] 获取数据，若不存在则返回 `null`。
     */
    fun <T : Any> get(key: String, type: PersistentDataType<*, T>): T? {
        return data.get(createKey(key), type)
    }

    /**
     * 通过 [key] 获取数据，若不存在则返回 [default]。
     */
    fun <T : Any> getOrDefault(key: String, type: PersistentDataType<*, T>, default: T): T {
        return data.getOrDefault(createKey(key), type, default)
    }

    /**
     * 通过 [key] 获取数据，若不存在则抛出异常。
     *
     * @throws IllegalArgumentException 若数据不存在
     */
    fun <T : Any> getOrThrow(key: String, type: PersistentDataType<*, T>): T {
        return this.get(key, type) ?: noSuchElm("Item tag with key $key (for $type) is not found.")
    }

    /**
     * 设置 [key] 相应的数据，若已有数据则覆盖。
     */
    fun <T : Any> set(key: String, type: PersistentDataType<*, T>, value: T) {
        data.set(createKey(key), type, value)
    }

    /**
     * 根据 [key] 查找是否存在数据。
     */
    fun <T : Any> has(key: String, type: PersistentDataType<*, T>): Boolean {
        return data.has(createKey(key), type)
    }

    /**
     * 删除 [key] 对应的数据，若不存在则不作处理。
     */
    fun remove(key: String) {
        data.remove(createKey(key))
    }

    /**
     * 应用更改后的 [ItemMeta] 到 [ItemStack]。
     *
     * @throws IllegalArgumentException 当 [ItemMeta] 不兼容该物品时
     */
    fun apply(item: ItemStack) {
        if (item.setItemMeta(meta)) return

        val metaCls = meta.javaClass.simpleName
        val type = item.type
        illegalArg("Item meta ($metaCls) cannot be applied to this item (type=$type)")
    }

    /**
     * 根据 [AkiraPlugin] 实例与 [key] 生成专属 [NamespacedKey]。
     */
    private fun createKey(key: String): NamespacedKey = NamespacedKey(plugin, key)

    companion object {
        /**
         * 使用 `DSL` 语法编辑标签并自动应用到同一个 [ItemStack] 上。
         */
        fun edit(plugin: AkiraPlugin, item: ItemStack, block: ItemTagEditor.() -> Unit) {
            ItemTagEditor(plugin, item).apply(block).apply(item)
        }
    }
}
package com.akira.core.api.util.item

import com.akira.core.api.AkiraPlugin
import com.akira.core.api.util.general.specifyUniqueId
import com.akira.core.api.util.world.specifyAttributeModifier
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * 物品属性编辑器
 *
 * 封装了对于 [ItemMeta.attributeModifiers] 的基础操作。
 *
 * @property meta 编辑对象
 * @property attribute 编辑属性类型
 * @property namespace 命名空间，用于生成修饰符的UUID
 */
class ItemAttributeEditor(
    private val meta: ItemMeta,
    private val attribute: Attribute,
    private val namespace: String?
) {
    /**
     * 获取 [ItemMeta.attributeModifiers]，若为null则返回一个空列表。
     */
    private val modifiers: Collection<AttributeModifier>
        get() = meta.attributeModifiers?.let { it[attribute] } ?: listOf()

    /**
     * 删除掉相应名称的UUID。
     *
     * @param name 用于结合 [namespace] 生成UUID的名称
     * @return 若其存在则删除后返回true，否则返回false
     */
    fun remove(name: String): Boolean {
        val filtered = modifiers.filter { it.uniqueId == specifyUniqueId(name, namespace) }
        if (filtered.isEmpty()) return false

        filtered.forEach { meta.removeAttributeModifier(attribute, it) }
        return true
    }

    /**
     * 按名称设置一份修饰符，若已存在将覆盖。
     *
     * @param name 用于结合 [namespace] 生成UUID的名称
     * @param value 修饰值
     * @param operation 修饰方式
     */
    fun set(name: String, value: Double, operation: Operation) {
        val modifier = specifyAttributeModifier(name, value, operation, namespace)

        this.remove(name)
        meta.addAttributeModifier(attribute, modifier)
    }

    /**
     * 按名称判断修饰符是否已存在。
     *
     * @param name 用于结合 [namespace] 生成UUID的名称
     * @return 若存在则返回true，否则返回false
     */
    fun contains(name: String): Boolean {
        return modifiers.any { it.uniqueId == specifyUniqueId(name, namespace) }
    }

    /**
     * 将更改应用至物品实例。
     *
     * @param item 物品实例
     */
    fun apply(item: ItemStack) = meta.let { item.itemMeta = it }

    companion object {
        /**
         * 为一个物品实例创建属性编辑器。
         *
         * 该工厂方法自动获取 [ItemStack.itemMeta]，并结合 [AkiraPlugin.name] 创建属性编辑器。
         *
         * @param item 编辑对象
         * @param attribute 编辑属性类型
         * @param plugin 插件实例
         * @return 创建好的属性编辑器实例
         * @throws IllegalArgumentException 当物品实例不支持该操作
         */
        fun forItemMeta(item: ItemStack, attribute: Attribute, plugin: AkiraPlugin) =
            ItemAttributeEditor(requireValidMeta(item), attribute, plugin.name)
    }
}
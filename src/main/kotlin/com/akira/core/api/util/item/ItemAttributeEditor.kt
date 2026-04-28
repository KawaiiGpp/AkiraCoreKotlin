package com.akira.core.api.util.item

import com.akira.core.api.AkiraPlugin
import com.akira.core.api.util.general.illegalArg
import com.akira.core.api.util.general.specifyUniqueId
import com.akira.core.api.util.world.specifyAttributeModifier
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

/**
 * 物品属性编辑器
 *
 * - 封装 [ItemMeta.attributeModifiers] 的操作
 * - [namespace] 用于配合修饰符名生成独立 [UUID]
 *
 * @param meta 编辑对象
 * @param attribute 编辑属性类型
 * @param namespace 命名空间
 */
class ItemAttributeEditor(
    private val meta: ItemMeta,
    private val attribute: Attribute,
    private val namespace: String?
) {
    /**
     * 为 [item] 创建关于 [attribute] 属性的编辑器。
     *
     * - 自动获取并验证 [ItemStack.itemMeta]
     * - 自动以 [AkiraPlugin.name] 为 [namespace]
     *
     * @throws IllegalStateException 当 [item] 不适用编辑器
     */
    constructor(item: ItemStack, attribute: Attribute, plugin: AkiraPlugin)
            : this(item.requiredMeta, attribute, plugin.name)

    /**
     * 获取 [ItemMeta.attributeModifiers]，若为 `null` 则返回一个空列表。
     */
    private val modifiers: Collection<AttributeModifier>
        get() = meta.attributeModifiers?.let { it[attribute] } ?: listOf()

    /**
     * 移除名为 [name] 的修饰符。
     * @return 若存在则移除并返回 `true`，否则返回 `false`
     */
    fun remove(name: String): Boolean {
        val filtered = modifiers.filter { it.uniqueId == specifyUniqueId(name, namespace) }
        if (filtered.isEmpty()) return false

        filtered.forEach { meta.removeAttributeModifier(attribute, it) }
        return true
    }

    /**
     * 新增修饰符，若已存在则覆盖。
     */
    fun set(
        name: String,
        value: Double,
        operation: Operation = Operation.ADD_NUMBER,
        slot: EquipmentSlotGroup = EquipmentSlotGroup.ANY
    ) {
        val modifier = specifyAttributeModifier(name, namespace, value, operation, slot)

        this.remove(name)
        meta.addAttributeModifier(attribute, modifier)
    }

    /**
     * 判断名为 [name] 的修饰符是否已存在。
     */
    fun contains(name: String): Boolean {
        return modifiers.any { it.uniqueId == specifyUniqueId(name, namespace) }
    }

    /**
     * 应用更改后的 [ItemMeta] 到 [item]。
     *
     * @throws IllegalArgumentException 当 [meta] 不兼容 [item]
     */
    fun apply(item: ItemStack) {
        if (item.setItemMeta(meta)) return

        val metaCls = meta.javaClass.simpleName
        val type = item.type
        illegalArg("Item meta ($metaCls) cannot be applied to this item (type=$type)")
    }
}
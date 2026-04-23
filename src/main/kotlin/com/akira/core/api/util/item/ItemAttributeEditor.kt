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

/**
 * 物品属性编辑器
 *
 * - 封装了对于 [ItemMeta.attributeModifiers] 的基础操作
 * - 属性 [namespace] 用于配合修饰符名称生成更独立的 `UUID`
 *
 * @property meta 编辑对象
 * @property attribute 编辑属性类型
 * @property namespace 命名空间
 */
class ItemAttributeEditor(
    private val meta: ItemMeta,
    private val attribute: Attribute,
    private val namespace: String?
) {
    /**
     * 为 [ItemStack] 创建属性编辑器。
     *
     * - 自动获取并验证 [ItemStack.itemMeta]
     * - 自动以 [AkiraPlugin.name] 为 [namespace]
     *
     * @param item 编辑对象
     * @param attribute 编辑属性类型
     * @param plugin 所属插件
     * @throws IllegalArgumentException 当物品实例不合法
     */
    constructor(item: ItemStack, attribute: Attribute, plugin: AkiraPlugin)
            : this(requireValidMeta(item), attribute, plugin.name)

    /**
     * 获取 [ItemMeta.attributeModifiers]，若为 `null` 则返回一个空列表。
     */
    private val modifiers: Collection<AttributeModifier>
        get() = meta.attributeModifiers?.let { it[attribute] } ?: listOf()

    /**
     * 移除对应的修饰符。
     *
     * @param name 修饰符名称
     * @return 若其存在则删除后返回 `true`，否则返回 `false`
     */
    fun remove(name: String): Boolean {
        val filtered = modifiers.filter { it.uniqueId == specifyUniqueId(name, namespace) }
        if (filtered.isEmpty()) return false

        filtered.forEach { meta.removeAttributeModifier(attribute, it) }
        return true
    }

    /**
     * 新增修饰符，若已存在则覆盖。
     *
     * @param name 修饰符名称
     * @param value 修饰值
     * @param operation 修饰行为
     * @param slot 生效槽位，默认 [EquipmentSlotGroup.ANY]
     */
    fun set(
        name: String,
        value: Double,
        operation: Operation,
        slot: EquipmentSlotGroup = EquipmentSlotGroup.ANY
    ) {
        val modifier = specifyAttributeModifier(name, namespace, value, operation, slot)

        this.remove(name)
        meta.addAttributeModifier(attribute, modifier)
    }

    /**
     * 判断修饰符是否已存在。
     *
     * @param name 修饰符名称
     * @return 若存在则返回 `true`，否则返回 `false`
     */
    fun contains(name: String): Boolean {
        return modifiers.any { it.uniqueId == specifyUniqueId(name, namespace) }
    }

    /**
     * 尝试应用更改后的 [ItemMeta] 到 [ItemStack]。
     *
     * @param item 物品实例
     * @throws IllegalArgumentException 当 [ItemMeta] 不兼容该物品时
     */
    fun apply(item: ItemStack) {
        if (item.setItemMeta(meta)) return

        val metaCls = meta.javaClass.simpleName
        val type = item.type
        illegalArg("Item meta ($metaCls) cannot be applied to this item (type=$type)")
    }
}
package com.akira.core.api.util.world

import com.akira.core.api.util.general.specifyUniqueId
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.EquipmentSlotGroup

/**
 * 新建一个可确定 `UUID` 的 [AttributeModifier]。
 *
 * - 内部调用 [specifyUniqueId] 生成 `UUID`
 *
 * @param name 修饰符名称
 * @param namespace 命名空间，默认为 `null`
 * @param value 修饰符值，默认为 `0`
 * @param operation 修饰符行为，默认为 [Operation.ADD_NUMBER]
 * @param slot 修饰符应用槽位，默认为 [EquipmentSlotGroup.ANY]
 * @see specifyUniqueId
 */
fun specifyAttributeModifier(
    name: String,
    namespace: String? = null,
    value: Double = 0.0,
    operation: Operation = Operation.ADD_NUMBER,
    slot: EquipmentSlotGroup = EquipmentSlotGroup.ANY
): AttributeModifier {
    val uniqueId = specifyUniqueId(name, namespace)

    return AttributeModifier(uniqueId, name, value, operation, slot)
}
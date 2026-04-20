package com.akira.core.api.util.entity

import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity

/**
 * 实体 [Attribute.GENERIC_MAX_HEALTH] 的属性实例
 *
 * @throws IllegalArgumentException 当实体不支持该属性
 */
val LivingEntity.maxHealthAttribute get() = this.requireAttribute(Attribute.GENERIC_MAX_HEALTH)

/**
 * 实体的基础生命值上限
 *
 * 未经过 [AttributeModifier] 计算的基础值。
 */
var LivingEntity.baseMaxHealth
    get() = maxHealthAttribute.baseValue
    set(value) = value.let { maxHealthAttribute.baseValue = it }

/**
 * 实体的最终生命值上限
 *
 * 经过 [AttributeModifier] 计算后的最终值。
 */
val LivingEntity.finalMaxHealth get() = maxHealthAttribute.value

/**
 * 重置实体的生命值上限。
 *
 * 抹除其 [AttributeInstance] 中的所有 [AttributeModifier]。
 */
fun LivingEntity.clearMaxHealthModifiers() {
    val modifiers = maxHealthAttribute.modifiers
    modifiers.forEach(maxHealthAttribute::removeModifier)
}

/**
 * 安全获取实体的 [AttributeInstance]。
 *
 * @throws IllegalArgumentException 当实体不支持该属性
 */
fun LivingEntity.requireAttribute(attribute: Attribute): AttributeInstance {
    val result = this.getAttribute(attribute)
    return requireNotNull(result) { "Attribute $attribute not supported for $type." }
}

/**
 * 实体是否举起盾牌
 *
 * 仅指示实体是否举盾，而非是否进入盾牌防御状态。
 */
val LivingEntity.isShieldRaised get() = isHandRaised && activeItem.type == Material.SHIELD
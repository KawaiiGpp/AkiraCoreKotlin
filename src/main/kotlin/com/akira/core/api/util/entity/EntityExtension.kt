package com.akira.core.api.util.entity

import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.entity.LivingEntity

var LivingEntity.baseMaxHealth
    get() = maxHealthAttribute.baseValue
    set(value) = value.let { maxHealthAttribute.baseValue = it }

val LivingEntity.finalMaxHealth get() = maxHealthAttribute.value

fun LivingEntity.clearMaxHealthModifiers() {
    val modifiers = maxHealthAttribute.modifiers
    modifiers.forEach(maxHealthAttribute::removeModifier)
}

/**
 * 实体 [Attribute.GENERIC_MAX_HEALTH] 的属性实例
 *
 * @throws IllegalArgumentException 当实体不支持该属性
 */
val LivingEntity.maxHealthAttribute get() = this.requireAttribute(Attribute.GENERIC_MAX_HEALTH)

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
package com.akira.core.api.util.entity

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.entity.LivingEntity

fun LivingEntity.getBaseMaxHealth(): Double =
    this.getNonNullAttribute(Attribute.GENERIC_MAX_HEALTH).baseValue

fun LivingEntity.getFinalMaxHealth(): Double =
    this.getNonNullAttribute(Attribute.GENERIC_MAX_HEALTH).value

fun LivingEntity.setBaseMaxHealth(value: Double) =
    value.let { this.getNonNullAttribute(Attribute.GENERIC_MAX_HEALTH).baseValue = it }

fun LivingEntity.resetMaxHealthModifiers() {
    val attribute = this.getNonNullAttribute(Attribute.GENERIC_MAX_HEALTH)
    attribute.modifiers.forEach { attribute.removeModifier(it) }
}

fun LivingEntity.getNonNullAttribute(attribute: Attribute): AttributeInstance =
    requireNotNull(this.getAttribute(attribute)) { "Attribute not supported for $type" }
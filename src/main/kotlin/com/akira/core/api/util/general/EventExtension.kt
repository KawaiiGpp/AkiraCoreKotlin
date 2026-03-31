package com.akira.core.api.util.general

import com.google.common.base.Function
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier

@Suppress("DEPRECATION", "UNCHECKED_CAST")
class EntityDamageEventReflect(private val event: EntityDamageEvent) {
    val modifiers
        get() = field("modifiers") as MutableMap<DamageModifier, Double>

    val modifierFunctions
        get() = field("modifierFunctions") as MutableMap<DamageModifier, Function<Double, Double>>

    val originals
        get() = field("originals") as MutableMap<DamageModifier, Double>

    private fun field(fieldName: String): Any? {
        val clz = EntityDamageEvent::class.java
        val field = clz.getDeclaredField(fieldName)

        field.isAccessible = true
        return field.get(event)
    }
}

val EntityDamageEvent.reflect get() = EntityDamageEventReflect(this)
package com.akira.core.api.util.general

import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import kotlin.math.min

@Suppress("DEPRECATION")
fun EntityDamageEvent.bypassVanillaModifiers(bypassAbsorption: Boolean = false) {
    val modifiers = DamageModifier.entries
        .filter { it != DamageModifier.BASE }
        .filter { this.isApplicable(it) }

    for (modifier in modifiers) {
        if (modifier == DamageModifier.ABSORPTION && !bypassAbsorption) {
            val entity = this.entity as? LivingEntity
            entity?.let { setDamage(modifier, -min(it.absorptionAmount, damage)) }
        } else {
            this.setDamage(modifier, 0.0)
        }
    }
}
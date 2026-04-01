package com.akira.core.api.util.general

import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import kotlin.math.min

fun <T> Iterable<T>.randomWeighted(transform: (T) -> Int): T {
    val list = map {
        it to transform(it)
            .apply { require(this > 0) { "Weight for $it is $this (It must be > 0)." } }
    }.also { require(it.isNotEmpty()) { "No elements to get randomly." } }

    val sum = list.sumOf { it.second }
    val point = globalRandom.nextInt(sum) + 1
    var weightCounter = 0

    list.forEach { (element, weight) ->
        weightCounter += weight
        if (point <= weightCounter) return element
    }

    throw UnsupportedOperationException("Unreachable code executed.")
}

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
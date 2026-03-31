package com.akira.core.api.util.general

import com.google.common.base.Function
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
fun EntityDamageEvent.enableTrueDamage() {
    val whitelist = setOf(DamageModifier.BASE, DamageModifier.ABSORPTION)
    val reflect = this.reflect
    fun predicate(entry: Map.Entry<DamageModifier, *>) = entry.key in whitelist

    reflect.originals.entries.retainAll { predicate(it) }

    val modifiers = reflect.modifiers
    val functions = reflect.modifierFunctions
    val entity = this.entity

    modifiers.entries.retainAll { predicate(it) }
    functions.entries.retainAll { predicate(it) }

    if (entity !is LivingEntity) return
    if (!this.isApplicable(DamageModifier.ABSORPTION)) return

    modifiers[DamageModifier.ABSORPTION] = -min(entity.absorptionAmount, damage)
    functions[DamageModifier.ABSORPTION] = Function { 0.0 }
}
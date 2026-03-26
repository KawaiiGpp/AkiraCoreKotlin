package com.akira.core.api.util.general

import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier

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

    fun filter(fieldName: String) {
        val clz = EntityDamageEvent::class.java
        val field = clz.getDeclaredField(fieldName)

        field.isAccessible = true
        val map = field.get(this) as MutableMap<*, *>

        map.entries.retainAll { whitelist.contains(it.key) }
    }

    filter("modifiers")
    filter("modifierFunctions")
    filter("originals")
}
package com.akira.core.api.util.general

import java.util.*
import kotlin.math.roundToInt

val globalRandom = Random()

fun specifyUniqueId(source: String, namespace: String? = null): UUID =
    UUID.nameUUIDFromBytes("$namespace:$source".toByteArray(Charsets.UTF_8))

fun rollChance(chance: Int): Boolean =
    chance.also {
        require(it in 0..100) { "Chance out of range [0,100]: $it" }
    }.let { globalRandom.nextInt(100) + 1 <= chance }

fun rollChance(chance: Double): Boolean =
    chance.also {
        require(it in 0.0..1.0) { "Chance out of range [0D,1D]: $it" }
    }.let { rollChance((it * 100).roundToInt()) }

inline fun <reified T : Enum<T>> nullableEnumOf(name: String): T? =
    enumValues<T>().firstOrNull { it.name == name }

fun <T> randomSublist(list: List<T>, amount: Int): List<T> {
    require(list.isNotEmpty()) { "Cannot get sublists from an empty collection." }
    require(amount <= list.size) { "The amount required > the actual size of the list." }

    return list.shuffled(globalRandom).take(amount)
}

fun <T> randomWeightedSublist(list: List<T>, amount: Int, transform: (T) -> Int): List<T> {
    require(list.isNotEmpty()) { "Cannot get sublists from an empty collection." }
    require(amount <= list.size) { "The amount required > the actual size of the list." }

    val copy = list.toMutableList()
    val result = mutableListOf<T>()
    repeat(amount) {
        copy.randomWeighted(transform)
            .also { result.add(it) }
            .also { copy.remove(it) }
    }

    return result
}

fun <T> requireLegit(value: T, predicate: (T) -> Boolean, message: ((T) -> String)? = null): T =
    requireNotNull(value.takeIf(predicate)) { message?.invoke(value) ?: "Value not legit: $value" }
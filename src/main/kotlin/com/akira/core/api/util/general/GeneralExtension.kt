package com.akira.core.api.util.general

fun <T> Iterable<T>.randomWeighted(transform: (T) -> Int): T {
    val list = map {
        it to transform(it)
            .apply { require(this > 0) { "Weight for $it is $this (It must be > 0)." } }
    }.also { require(it.isNotEmpty()) {"No elements to get randomly."} }

    val sum = list.sumOf { it.second }
    val point = randomController.nextInt(sum) + 1
    var weightCounter = 0

    list.forEach { (element, weight) ->
        weightCounter += weight
        if (point <= weightCounter) return element
    }

    throw UnsupportedOperationException("Unreachable code executed.")
}
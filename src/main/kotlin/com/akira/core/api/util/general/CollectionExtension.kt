package com.akira.core.api.util.general

import kotlin.random.Random

/**
 * 对列表进行随机批量采样。
 *
 * @param amount 样本数量，取值范围 `0..size`
 * @return 随机样本列表
 * @throws IllegalArgumentException 当 [amount] 为负数或大于被采样列表的 [List.size] 时抛出
 */
fun <T> List<T>.randomSample(amount: Int): List<T> {
    require(amount >= 0) { "Sublist's size must be >= 0, but actual: $amount." }
    require(amount <= size) { "Sublist's size ($amount) > original list's size ($size)." }

    if (amount == size) return this.toList()
    if (amount == 0) return listOf()
    if (amount >= size / 2) return shuffled().take(amount)

    val indexes = mutableSetOf<Int>()
    while (indexes.size < amount) indexes.add(Random.nextInt(size))

    return indexes.map { this[it] }
}

/**
 * 对列表进行加权随机采样。
 *
 * @param transform 权重获取函数
 * @return 加权采样结果
 * @throws IllegalArgumentException 当被采样列表为空，或所有元素的权重值之总和等于零时抛出
 */
fun <T> Collection<T>.randomWeighted(transform: (T) -> Int): T {
    require(this.isNotEmpty()) { "No elements to get randomly." }

    val total = this.sumOf {
        val weight = transform(it)

        require(weight >= 0) { "Weight for $it < 0: $weight." }
        weight.toLong()
    }

    require(total > 0) { "Total weight must be > 0, but actual: $total." }

    val point = Random.nextLong(total)
    var accumulator = 0L

    this.forEach {
        accumulator += transform(it)
        if (point < accumulator) return it
    }

    throw IllegalStateException("Internal error: It should never reach here.")
}
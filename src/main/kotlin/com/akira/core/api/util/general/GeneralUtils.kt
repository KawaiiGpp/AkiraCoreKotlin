package com.akira.core.api.util.general

import java.util.*
import kotlin.enums.enumEntries

val globalRandom = Random()

/**
 * 通过字符串生成一个确定的 [UUID] 对象。
 *
 * 该方法基于 [UUID.nameUUIDFromBytes]。
 *
 * 允许用户传入 [namespace] 参与生成，
 * 避免不同场景 [source] 意外相同生成出相同 [UUID] 的情况，
 *
 * 当 [namespace] 为 `null` 时不参与生成。
 *
 * @param source 字符串源
 * @param namespace 命名空间，默认为 `null`
 * @return 基于 [source] 与 [namespace] 生成的 [UUID]
 */
fun specifyUniqueId(source: String, namespace: String? = null): UUID =
    UUID.nameUUIDFromBytes("$namespace:$source".toByteArray(Charsets.UTF_8))

/**
 * 为无分隔符的 [UUID] 字符串插入分隔符。
 *
 * @param raw 待处理字符串
 * @return 插入分隔符后的字符串
 * @throws IllegalArgumentException 当输入的字符串长度错误或包含空格的时候抛出
 */
fun separateUniqueId(raw: String): String {
    require(raw.length == 32) { "Wrong length for unique id: $raw" }
    require(!raw.contains(Regex("\\s"))) { "Raw unique id cannot contain any space." }

    return StringBuilder(raw)
        .apply {
            insert(23, "-")
            insert(18, "-")
            insert(13, "-")
            insert(8, "-")
        }.toString()
}

/**
 * 根据概率值随机返回布尔值。
 *
 * @param chance 概率值，取值范围 `0-100`
 * @return 基于 [chance] 的随机布尔值
 * @throws IllegalArgumentException 当 `chance` 不在 `0-100` 的取值范围内时
 */
fun rollChance(chance: Int): Boolean {
    require(chance in 0..100) { "Chance out of range [0, 100]: $chance" }
    return globalRandom.nextInt(100) < chance
}

/**
 * 根据概率值随机返回布尔值。
 *
 * @param chance 概率值，取值范围 `0.0-1.0`
 * @return 基于 [chance] 的随机布尔值
 * @throws IllegalArgumentException 当 `chance` 不在 `0.0-1.0` 的取值范围内时
 */
fun rollChance(chance: Double): Boolean {
    require(chance in 0.0..1.0) { "Chance out of range [0.0, 1.0]: $chance" }
    return globalRandom.nextDouble() < chance
}

/**
 * 根据名称来查找特定的枚举常量。
 *
 * @param T 想要查询的枚举类型
 * @param name 枚举名称
 * @return 符合名称的枚举实例，不存在则返回 `null`
 */
inline fun <reified T : Enum<T>> nullableEnumOf(name: String): T? =
    enumEntries<T>().firstOrNull { it.name == name }

fun <T> randomSublist(list: List<T>, amount: Int): List<T> {
    require(list.isNotEmpty()) { "Cannot get sublist from an empty collection." }
    require(amount <= list.size) { "Sublist's size ($amount) > original list's size (${list.size})." }

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
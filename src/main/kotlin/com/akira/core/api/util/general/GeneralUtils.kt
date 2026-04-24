package com.akira.core.api.util.general

import java.util.*
import kotlin.enums.enumEntries
import kotlin.random.Random

/**
 * 通过 [source] 与 [namespace] 生成确定的 [UUID]。
 *
 * - 基于 [UUID.nameUUIDFromBytes]
 * - 传入 [namespace] 可增加 [UUID] 唯一性
 *
 * @param source 字符串源
 * @param namespace 命名空间，默认 `null`
 */
fun specifyUniqueId(source: String, namespace: String? = null): UUID {
    return UUID.nameUUIDFromBytes("$namespace:$source".toByteArray(Charsets.UTF_8))
}

/**
 * 为无分隔符 [UUID] 字符串插入分隔符。
 *
 * @throws IllegalArgumentException 当输入字符串长度错误或包含空格的时候抛出
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
 * 根据概率值 [chance] 返回随机布尔值。
 *
 * @param chance 概率值，范围 `0-100`
 * @throws IllegalArgumentException 当 [chance] 不在 `0-100` 的取值范围内时
 */
fun rollChance(chance: Int): Boolean {
    require(chance in 0..100) { "Chance out of range [0, 100]: $chance" }
    return Random.nextInt(100) < chance
}

/**
 * 根据概率值 [chance] 返回随机布尔值。
 *
 * @param chance 概率值，范围 `0.0-1.0`
 * @throws IllegalArgumentException 当 [chance] 不在 `0.0-1.0` 的取值范围内时
 */
fun rollChance(chance: Double): Boolean {
    require(chance in 0.0..1.0) { "Chance out of range [0.0, 1.0]: $chance" }
    return Random.nextDouble() < chance
}

/**
 * 根据 [name] 查找 [T] 的枚举实例，不存在则返回 `null`。
 */
inline fun <reified T : Enum<T>> nullableEnumOf(name: String): T? {
    return enumEntries<T>().firstOrNull { it.name == name }
}
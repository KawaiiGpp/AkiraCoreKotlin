package com.akira.core.api.util.general

import java.util.*
import kotlin.enums.enumEntries
import kotlin.random.Random

/**
 * 通过字符串生成一个确定的 [UUID] 对象。
 *
 * - 基于 [UUID.nameUUIDFromBytes]
 * - 可传入 [namespace] 增加 [UUID] 唯一性
 *
 * 当 [namespace] 为 `null` 时不参与生成。
 *
 * @param source 字符串源
 * @param namespace 命名空间，默认 `null`
 * @return 基于输入的确定性 [UUID]
 */
fun specifyUniqueId(source: String, namespace: String? = null): UUID =
    UUID.nameUUIDFromBytes("$namespace:$source".toByteArray(Charsets.UTF_8))

/**
 * 为无分隔符 [UUID] 字符串插入分隔符。
 *
 * @param raw 原字符串
 * @return 插入分隔符后的字符串
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
 * 根据概率值随机返回布尔值。
 *
 * @param chance 概率值，范围 `0-100`
 * @return 基于 [chance] 的随机布尔值
 * @throws IllegalArgumentException 当 `chance` 不在 `0-100` 的取值范围内时
 */
fun rollChance(chance: Int): Boolean {
    require(chance in 0..100) { "Chance out of range [0, 100]: $chance" }
    return Random.nextInt(100) < chance
}

/**
 * 根据概率值随机返回布尔值。
 *
 * @param chance 概率值，范围 `0.0-1.0`
 * @return 基于 [chance] 的随机布尔值
 * @throws IllegalArgumentException 当 `chance` 不在 `0.0-1.0` 的取值范围内时
 */
fun rollChance(chance: Double): Boolean {
    require(chance in 0.0..1.0) { "Chance out of range [0.0, 1.0]: $chance" }
    return Random.nextDouble() < chance
}

/**
 * 校验 [value] 是否合法。
 *
 * 若通过校验，则返回原值，否则抛出 [IllegalArgumentException]。
 *
 * @param value 待校验的值
 * @param predicate 校验逻辑
 * @param message 错误提示逻辑，传入 [value] 作为参数
 * @return 校验通过后返回的 [value] 原值
 * @throws IllegalArgumentException 当校验不通过时抛出
 */
inline fun <T> requireLegit(
    value: T,
    predicate: (T) -> Boolean,
    message: (T) -> String = { "Value not legit: $it" }
): T {
    require(predicate(value)) { message(value) }
    return value
}

/**
 * 根据名称查找特定的枚举常量。
 *
 * @param T 想要查询的枚举类型
 * @param name 枚举名称
 * @return 符合名称的枚举实例，不存在则返回 `null`
 */
inline fun <reified T : Enum<T>> nullableEnumOf(name: String): T? =
    enumEntries<T>().firstOrNull { it.name == name }
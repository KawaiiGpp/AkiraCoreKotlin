package com.akira.core.api.util.math

import com.akira.core.api.util.general.ensure
import com.akira.core.api.util.general.unsprtOpera
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * 将数值四舍五入并保留位数为 [decimalAmount] 位。
 *
 * @throws IllegalArgumentException 当数值非法或位数为负
 */
inline fun <reified T : Number> T.simplify(decimalAmount: Int): T {
    this.requireLegit("Number to be simplified")
    decimalAmount.requireNonNegative("Decimal amount")

    val decimal = BigDecimal(this.toString()).setScale(decimalAmount, RoundingMode.HALF_UP)

    return when (this) {
        is Double -> decimal.toDouble() as T
        is Float -> decimal.toFloat() as T
        is Int -> decimal.toInt() as T
        is Long -> decimal.toLong() as T
        is Short -> decimal.toShort() as T
        is Byte -> decimal.toByte() as T
        else -> unsprtOpera("Unsupported type to simplify: ${javaClass.name}")
    }
}

/**
 * 将数值转为 [decimalAmount] 位小数的字符串。
 *
 * @throws IllegalArgumentException 当 [decimalAmount] 为负时抛出
 */
fun <T : Number> T.format(decimalAmount: Int = 2): String {
    decimalAmount.requireNonNegative("Decimal amount")
    val value = this.toDouble()

    return if (decimalAmount == 0) "%,.0f".format(value)
    else "%,.${decimalAmount}f".format(value)
}

/**
 * 判断数值是否不为 `NaN` 或 `±Infinity`。
 */
fun Number.isLegit(): Boolean =
    when (this) {
        is Double -> !this.isNaN() && !this.isInfinite()
        is Float -> !this.isNaN() && !this.isInfinite()
        else -> true
    }

/**
 * 判断数值是否 [isLegit] 且为正数。
 */
fun Number.isPositive(): Boolean = this.isLegit() && this.toDouble() > 0

/**
 * 判断数值是否 [isLegit] 且为非负数。
 */
fun Number.isNonNegative(): Boolean = this.isLegit() && this.toDouble() >= 0

/**
 * 断言数值不为 `NaN` 或 `±Infinity`。
 */
inline fun <reified T : Number> T.requireLegit(argumentName: String): T {
    return ensure(Number::isLegit) {
        "$argumentName is not legit: $it (Type: ${javaClass.name})"
    }
}

/**
 * 断言数值 [isLegit] 且为正数。
 */
inline fun <reified T : Number> T.requirePositive(argumentName: String): T {
    return ensure(Number::isPositive) {
        "$argumentName must be positive, but actual: $it (Type: ${javaClass.name})"
    }
}

/**
 * 断言数值 [isLegit] 且为非负数。
 */
inline fun <reified T : Number> T.requireNonNegative(argumentName: String): T {
    return ensure(Number::isNonNegative) {
        "$argumentName must be non-negative, but actual: $it (Type: ${javaClass.name})"
    }
}
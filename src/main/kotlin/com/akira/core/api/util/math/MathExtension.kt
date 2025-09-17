package com.akira.core.api.util.math

import com.akira.core.api.util.general.requireLegit
import java.math.BigDecimal
import java.math.RoundingMode

inline fun <reified T : Number> T.simplify(decimalAmount: Int): T {
    this.requiresLegit()
    decimalAmount.requiresNonNegative()

    val decimal = BigDecimal.valueOf(this.toDouble())
        .setScale(decimalAmount, RoundingMode.HALF_UP)

    return when (this) {
        is Double -> decimal.toDouble() as T
        is Float -> decimal.toFloat() as T
        is Int -> decimal.toInt() as T
        is Long -> decimal.toLong() as T
        is Short -> decimal.toShort() as T
        is Byte -> decimal.toByte() as T
        else -> throw IllegalArgumentException("Unsupported type to simplify: ${javaClass.name}")
    }
}

fun Number.isLegit(): Boolean =
    when (this) {
        is Double -> !this.isNaN() && !this.isInfinite()
        is Float -> !this.isNaN() && !this.isInfinite()
        else -> true
    }

fun Number.isPositive(): Boolean = this.isLegit() && this.toDouble() > 0

fun Number.isNonNegative(): Boolean = this.isLegit() && this.toDouble() >= 0

inline fun <reified T : Number> T.requiresLegit(): T =
    requireLegit(this, Number::isLegit) { "Number not legit: $it (Type: ${javaClass.name})" }

inline fun <reified T : Number> T.requiresPositive(): T =
    requireLegit(this, Number::isPositive) { "Number must be positive: $it (Type: ${javaClass.name})" }

inline fun <reified T : Number> T.requiresNonNegative(): T =
    requireLegit(this, Number::isNonNegative) { "Number must be non-negative: $it (Type: ${javaClass.name})" }
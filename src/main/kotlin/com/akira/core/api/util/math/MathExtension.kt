package com.akira.core.api.util.math

import com.akira.core.api.util.general.requireLegit
import java.math.BigDecimal
import java.math.RoundingMode

fun Double.simplify(decimalAmount: Int): Double =
    BigDecimal.valueOf(this.also {
        require(it > 0) {
            "Decimal amount must be > 0."
        }
    }).setScale(decimalAmount, RoundingMode.HALF_UP)
        .toDouble()

fun Float.simplify(decimalAmount: Int): Float =
    BigDecimal.valueOf(this.toDouble().also {
        require(it > 0) {
            "Decimal amount must be > 0."
        }
    }).setScale(decimalAmount, RoundingMode.HALF_UP)
        .toFloat()

fun Double.isLegit(): Boolean = !this.isNaN() && !this.isInfinite()

fun Float.isLegit(): Boolean = !this.isNaN() && !this.isInfinite()

fun Double.requiresLegit(): Double = requireLegit(this, Double::isLegit) { "Double not legit: $it" }

fun Float.requiresLegit(): Float = requireLegit(this, Float::isLegit) { "Float not legit: $it" }

fun Double.isPositive(): Boolean = this > 0

fun Float.isPositive(): Boolean = this > 0

fun Double.requiresPositive(): Double = requireLegit(this, Double::isPositive) { "Double must be positive: $it" }

fun Float.requiresPositive(): Float = requireLegit(this, Float::isPositive) { "Float must be positive: $it" }

fun Double.isNonNegative(): Boolean = this >= 0

fun Float.isNonNegative(): Boolean = this >= 0

fun Double.requiresNonNegative(): Double = requireLegit(this, Double::isNonNegative) { "Double must be non-negative: $it" }

fun Float.requiresNonNegative(): Float = requireLegit(this, Float::isNonNegative) { "Float must be non-negative: $it" }
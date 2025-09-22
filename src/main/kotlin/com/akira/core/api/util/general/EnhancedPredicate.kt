package com.akira.core.api.util.general

class EnhancedPredicate<T : Any>(
    val failureMessage: String,
    val nullMessage: String? = null,
    val predicate: (T) -> Boolean
) {
    fun test(value: T?): PredicateResult<T> = when {
        value == null -> PredicateResult.createFailure(nullMessage ?: failureMessage)
        predicate(value) -> PredicateResult.createSuccess(value)
        else -> PredicateResult.createFailure(failureMessage)
    }
}
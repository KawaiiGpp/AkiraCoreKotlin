package com.akira.core.api.util.general

class PredicateResult<T : Any>
private constructor(val rawValue: T?, val rawFailureMessage: String?) {
    init {
        require(
            (rawValue == null && rawFailureMessage != null)
                    || (rawValue != null && rawFailureMessage == null)
        ) { "Illegal initialization." }
    }

    val value get() = requireNotNull(rawValue) { "No values from the result." }
    val failureMessage get() = requireNotNull(rawFailureMessage) { "No failure messages from the result." }

    val success = rawValue != null
    val failure = rawFailureMessage != null

    fun onSuccess(block: (T) -> Unit): PredicateResult<T> {
        if (success) block(value)
        return this
    }

    fun onFailure(block: (String) -> Unit): PredicateResult<T> {
        if (failure) block(failureMessage)
        return this
    }

    companion object {
        fun <T : Any> createSuccess(value: T): PredicateResult<T> = PredicateResult(value, null)

        fun <T : Any> createFailure(message: String): PredicateResult<T> = PredicateResult(null, message)
    }
}
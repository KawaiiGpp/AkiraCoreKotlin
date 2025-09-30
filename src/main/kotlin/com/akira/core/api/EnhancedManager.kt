package com.akira.core.api

abstract class EnhancedManager<E> : Manager<String, E>() {
    open fun register(element: E) = super.register(transform(element), element)

    protected abstract fun transform(element: E): String
}
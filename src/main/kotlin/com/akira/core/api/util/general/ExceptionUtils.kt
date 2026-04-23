package com.akira.core.api.util.general

fun illegalArg(message: String): Nothing = throw IllegalArgumentException(message)

fun illegalState(message: String): Nothing = throw IllegalStateException(message)

fun noSuchElm(message: String): Nothing = throw NoSuchElementException(message)
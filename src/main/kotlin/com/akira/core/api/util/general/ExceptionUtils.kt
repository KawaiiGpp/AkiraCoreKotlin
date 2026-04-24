package com.akira.core.api.util.general

import java.io.IOException

fun illegalArg(message: String): Nothing = throw IllegalArgumentException(message)

fun illegalState(message: String): Nothing = throw IllegalStateException(message)

fun noSuchElm(message: String): Nothing = throw NoSuchElementException(message)

fun nullPointer(message: String): Nothing = throw NullPointerException(message)

fun unsprtOpera(message: String): Nothing = throw UnsupportedOperationException(message)

fun ioFailure(message:String):Nothing = throw IOException(message)
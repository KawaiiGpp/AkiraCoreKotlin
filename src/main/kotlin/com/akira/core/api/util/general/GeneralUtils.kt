package com.akira.core.api.util.general

import java.util.*

fun specifyUniqueId(source: String, namespace: String? = null): UUID =
    UUID.nameUUIDFromBytes("$namespace:$source".toByteArray(Charsets.UTF_8))
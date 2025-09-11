package com.akira.core.api.util.general

import java.util.*

fun specifyUniqueId(source: String): UUID =
    UUID.nameUUIDFromBytes(source.toByteArray(Charsets.UTF_8))
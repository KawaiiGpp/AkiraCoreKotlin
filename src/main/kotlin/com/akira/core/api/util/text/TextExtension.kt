package com.akira.core.api.util.text

import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

fun String.toComponent(): TextComponent = LegacyComponentSerializer.legacySection().deserialize(this)

fun TextComponent.toLegacy(): String = LegacyComponentSerializer.legacySection().serialize(this)
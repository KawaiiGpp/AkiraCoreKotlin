package com.akira.core.api.util.text

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

fun String.toComponent(): TextComponent = LegacyComponentSerializer.legacySection().deserialize(this)

fun Audience.sendLine(length: Int, color: TextColor) = sendMessage(generateLine(length, color))

fun Audience.sendEmptyLine() = sendMessage(Component.text())
package com.akira.core.api.util.text

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import java.text.DecimalFormat

val globalDecimalFormat = DecimalFormat("#,##0.##")

fun debug(content: Any?) = Bukkit.broadcast(content.toString().toComponent())

fun generateLine(length: Int): String = buildString { repeat(length) { append('▬') } }

fun generateLine(length: Int, color: TextColor): TextComponent = Component.text(generateLine(length), color)
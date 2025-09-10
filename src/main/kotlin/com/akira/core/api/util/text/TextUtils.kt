package com.akira.core.api.util.text

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit

fun debug(content: Any?) = Bukkit.broadcast(content.toString().toComponent())

fun generateLine(length: Int): String = buildString { repeat(length) { append('▬') } }

fun generateLine(length: Int, color: NamedTextColor): TextComponent = Component.text(generateLine(length), color)
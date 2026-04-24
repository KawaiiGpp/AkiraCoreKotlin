package com.akira.core.api.util.text

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import java.text.DecimalFormat

/**
 * 统一数值格式化工具，格式示例：`1,234.56`。
 */
val globalDecimalFormat = DecimalFormat("#,##0.##")

/**
 * 直接广播 [content] 到聊天栏或日志，用于快捷调试。
 */
fun debug(content: Any?) = Bukkit.broadcast(content.toString().toComponent())

/**
 * 生成分割线，长度为 [length]。
 */
fun generateLine(length: Int): String = buildString { repeat(length) { append('▬') } }

/**
 * 生成分割线，颜色为 [color]，长度为 [length]。
 */
fun generateLine(length: Int, color: TextColor): TextComponent = Component.text(generateLine(length), color)
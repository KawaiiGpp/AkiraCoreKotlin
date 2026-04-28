package com.akira.core.api.util.player

import com.akira.core.api.util.math.requireNonNegative
import com.akira.core.api.util.text.toComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times
import org.bukkit.entity.Player
import java.time.Duration

/**
 * 向玩家展示标题信息。
 *
 * - 若屏幕上正在展示另一组标题信息，将覆盖
 * - [fadeIn]、[duration]、[fadeOut] 单位为游戏刻
 *
 * @throws IllegalArgumentException 当时长为负
 */
fun Player.showTitle(
    title: Component,
    subtitle: Component = Component.empty(),
    fadeIn: Long = 10,
    duration: Long = 80,
    fadeOut: Long = 10
) {
    fadeIn.requireNonNegative("Fade-in ticks")
    duration.requireNonNegative("Title ticks")
    fadeOut.requireNonNegative("Fade-out ticks")

    this.showTitle(
        Title.title(
            title,
            subtitle,
            Times.times(
                Duration.ofMillis(fadeIn * 50),
                Duration.ofMillis(duration * 50),
                Duration.ofMillis(fadeOut * 50)
            )
        )
    )
}

/**
 * 向玩家展示标题信息。
 *
 * - 自动将内容 [String] 转换为 [Component]
 *
 * @throws IllegalArgumentException 当时长为负
 */
fun Player.showTitle(
    title: String,
    subtitle: String? = null,
    fadeIn: Long = 10,
    duration: Long = 80,
    fadeOut: Long = 10
) {
    this.showTitle(
        title.toComponent(),
        subtitle?.toComponent() ?: Component.empty(),
        fadeIn,
        duration,
        fadeOut
    )
}
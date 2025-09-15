package com.akira.core.api.util.player

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times
import org.bukkit.entity.Player
import java.time.Duration

fun Player.showTitle(
    main: Component, sub: Component = Component.empty(),
    fadeIn: Long = 10, duration: Long = 80, fadeOut: Long = 10
) = this.showTitle(
    Title.title(
        main,
        sub,
        Times.times(
            Duration.ofMillis(fadeIn * 50),
            Duration.ofMillis(duration * 50),
            Duration.ofMillis(fadeOut * 50)
        )
    )
)
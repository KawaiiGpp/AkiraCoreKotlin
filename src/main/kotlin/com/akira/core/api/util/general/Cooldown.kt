package com.akira.core.api.util.general

import com.akira.core.api.AkiraPlugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

class Cooldown(val plugin: AkiraPlugin) {
    private var bukkitTask: BukkitTask? = null
    val inCooldown get() = bukkitTask != null

    fun start(ticks: Long): Boolean {
        if (inCooldown) return false

        bukkitTask = Bukkit.getScheduler().runTaskLater(plugin, { -> bukkitTask = null }, ticks)
        return true
    }

    fun restart(ticks: Long) {
        reset()
        start(ticks)
    }

    fun reset() {
        bukkitTask?.cancel()
        bukkitTask = null
    }
}
package com.akira.core.api

import org.bukkit.Bukkit

class TaskMaster(private val plugin: AkiraPlugin) {
    private val scheduler get() = Bukkit.getScheduler()

    fun run(block: () -> Unit) =
        scheduler.runTask(plugin, Runnable(block))

    fun run(later: Long, block: () -> Unit) =
        scheduler.runTaskLater(plugin, Runnable(block), later)

    fun run(later: Long, period: Long, block: () -> Unit) =
        scheduler.runTaskTimer(plugin, Runnable(block), later, period)

    fun runAsync(block: () -> Unit) =
        scheduler.runTaskAsynchronously(plugin, Runnable(block))

    fun runAsync(later: Long, block: () -> Unit) =
        scheduler.runTaskLaterAsynchronously(plugin, Runnable(block), later)

    fun runAsync(later: Long, period: Long, block: () -> Unit) =
        scheduler.runTaskTimerAsynchronously(plugin, Runnable(block), later, period)
}
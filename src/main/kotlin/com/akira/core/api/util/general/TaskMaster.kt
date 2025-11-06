package com.akira.core.api.util.general

import com.akira.core.api.AkiraPlugin
import org.bukkit.Bukkit

private val scheduler get() = Bukkit.getScheduler()

fun AkiraPlugin.run(block: () -> Unit) =
    scheduler.runTask(this, Runnable(block))

fun AkiraPlugin.run(later: Long, block: () -> Unit) =
    scheduler.runTaskLater(this, Runnable(block), later)

fun AkiraPlugin.run(later: Long, period: Long, block: () -> Unit) =
    scheduler.runTaskTimer(this, Runnable(block), later, period)

fun AkiraPlugin.runAsync(block: () -> Unit) =
    scheduler.runTaskAsynchronously(this, Runnable(block))

fun AkiraPlugin.runAsync(later: Long, block: () -> Unit) =
    scheduler.runTaskLaterAsynchronously(this, Runnable(block), later)

fun AkiraPlugin.runAsync(later: Long, period: Long, block: () -> Unit) =
    scheduler.runTaskTimerAsynchronously(this, Runnable(block), later, period)

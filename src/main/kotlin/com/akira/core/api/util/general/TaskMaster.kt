package com.akira.core.api.util.general

import com.akira.core.api.AkiraPlugin
import org.bukkit.Bukkit

private val scheduler get() = Bukkit.getScheduler()

fun AkiraPlugin.launch(block: () -> Unit) =
    scheduler.runTask(this, Runnable(block))

fun AkiraPlugin.launch(later: Long, block: () -> Unit) =
    scheduler.runTaskLater(this, Runnable(block), later)

fun AkiraPlugin.launch(later: Long, period: Long, block: () -> Unit) =
    scheduler.runTaskTimer(this, Runnable(block), later, period)

fun AkiraPlugin.launchAsync(block: () -> Unit) =
    scheduler.runTaskAsynchronously(this, Runnable(block))

fun AkiraPlugin.launchAsync(later: Long, block: () -> Unit) =
    scheduler.runTaskLaterAsynchronously(this, Runnable(block), later)

fun AkiraPlugin.launchAsync(later: Long, period: Long, block: () -> Unit) =
    scheduler.runTaskTimerAsynchronously(this, Runnable(block), later, period)

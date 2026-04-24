package com.akira.core.api.util.general

import com.akira.core.api.AkiraPlugin
import org.bukkit.Bukkit

private val scheduler get() = Bukkit.getScheduler()

/**
 * 在主线程执行一个任务。
 */
fun AkiraPlugin.launch(block: () -> Unit) =
    scheduler.runTask(this, Runnable(block))

/**
 * 主线程执行，[later] 刻后开始。
 */
fun AkiraPlugin.launch(later: Long, block: () -> Unit) =
    scheduler.runTaskLater(this, Runnable(block), later)

/**
 * 主线程执行，[later] 刻后开始，循环间隔 [period] 刻。
 */
fun AkiraPlugin.launch(later: Long, period: Long, block: () -> Unit) =
    scheduler.runTaskTimer(this, Runnable(block), later, period)

/**
 * 在线程池执行一个任务。
 */
fun AkiraPlugin.launchAsync(block: () -> Unit) =
    scheduler.runTaskAsynchronously(this, Runnable(block))

/**
 * 线程池执行，[later] 刻后开始。
 */
fun AkiraPlugin.launchAsync(later: Long, block: () -> Unit) =
    scheduler.runTaskLaterAsynchronously(this, Runnable(block), later)

/**
 * 线程池执行，[later] 刻后开始，循环间隔 [period] 刻。
 */
fun AkiraPlugin.launchAsync(later: Long, period: Long, block: () -> Unit) =
    scheduler.runTaskTimerAsynchronously(this, Runnable(block), later, period)

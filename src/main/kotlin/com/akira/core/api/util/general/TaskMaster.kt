package com.akira.core.api.util.general

import com.akira.core.api.AkiraPlugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask

private val scheduler get() = Bukkit.getScheduler()

/**
 * 在主线程执行一个任务。
 *
 * 基于 [BukkitScheduler.runTask] 实现。
 *
 * @param block 任务逻辑
 * @return 生成的 [BukkitTask] 实例
 */
fun AkiraPlugin.launch(block: () -> Unit) =
    scheduler.runTask(this, Runnable(block))

/**
 * 在主线程执行一个延时任务。
 *
 * 基于 [BukkitScheduler.runTaskLater] 实现。
 *
 * @param later 延迟时长，单位为游戏刻
 * @param block 任务逻辑
 * @return 生成的 [BukkitTask] 实例
 */
fun AkiraPlugin.launch(later: Long, block: () -> Unit) =
    scheduler.runTaskLater(this, Runnable(block), later)

/**
 * 在主线程执行一个计时循环任务。
 *
 * 基于 [BukkitScheduler.runTaskTimer] 实现。
 *
 * @param later 延迟时长，单位为游戏刻
 * @param period 循环间隔时长，单位为游戏刻
 * @param block 任务逻辑
 * @return 生成的 [BukkitTask] 实例
 */
fun AkiraPlugin.launch(later: Long, period: Long, block: () -> Unit) =
    scheduler.runTaskTimer(this, Runnable(block), later, period)

/**
 * 在线程池执行一个任务。
 *
 * 基于 [BukkitScheduler.runTaskAsynchronously] 实现。
 *
 * @param block 任务逻辑
 * @return 生成的 [BukkitTask] 实例
 */
fun AkiraPlugin.launchAsync(block: () -> Unit) =
    scheduler.runTaskAsynchronously(this, Runnable(block))

/**
 * 在线程池执行一个延时任务。
 *
 * 基于 [BukkitScheduler.runTaskLaterAsynchronously] 实现。
 *
 * @param later 延迟时长，单位为游戏刻
 * @param block 任务逻辑
 * @return 生成的 [BukkitTask] 实例
 */
fun AkiraPlugin.launchAsync(later: Long, block: () -> Unit) =
    scheduler.runTaskLaterAsynchronously(this, Runnable(block), later)

/**
 * 在线程池执行一个计时循环任务。
 *
 * 基于 [BukkitScheduler.runTaskTimerAsynchronously] 实现。
 *
 * @param later 延迟时长，单位为游戏刻
 * @param period 循环间隔时长，单位为游戏刻
 * @param block 任务逻辑
 * @return 生成的 [BukkitTask] 实例
 */
fun AkiraPlugin.launchAsync(later: Long, period: Long, block: () -> Unit) =
    scheduler.runTaskTimerAsynchronously(this, Runnable(block), later, period)

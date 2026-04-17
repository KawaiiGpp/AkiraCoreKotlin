package com.akira.core.api.util.general

import com.akira.core.api.AkiraPlugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask

/**
 * 冷却管理器
 *
 * 该工具基于 [BukkitScheduler] 与 [BukkitTask]，
 * 配合状态管理逻辑的封装，实现了简易的冷却管理。
 *
 * * 注意：该工具非线程安全。
 *
 * @property plugin 所属插件，用于注册调度任务
 */
class Cooldown(val plugin: AkiraPlugin) {
    /**
     * 延时任务，用于延时重置冷却状态
     *
     * 自身亦可指示冷却状态，
     * 非 `null` 代表正在冷却状态。
     *
     * 当冷却自然结束或被重置，将被设为 `null`。
     */
    private var bukkitTask: BukkitTask? = null

    /**
     * 指示当前实例是否处于冷却状态。
     *
     * @return 返回 `true` 即冷却中，`false` 即未在冷却
     */
    val inCooldown get() = bukkitTask != null

    /**
     * 启用冷却，期间 [inCooldown] 将返回 `true`。
     *
     * @param ticks 冷却时长，单位为游戏刻
     * @return 启用成功返回 `true`，`false` 表示已在冷却
     * @throws IllegalArgumentException 当 [ticks] 被传入小于或等于零的值
     */
    fun start(ticks: Long): Boolean {
        require(ticks > 0) { "Ticks for cooldown must be > 0, but actual: $ticks." }
        if (inCooldown) return false

        val scheduler = Bukkit.getScheduler()
        bukkitTask = scheduler.runTaskLater(plugin, { -> bukkitTask = null }, ticks)
        return true
    }

    /**
     * 重置冷却，并指定新的冷却时长。
     *
     * @param ticks 冷却时长，单位为游戏刻
     * @throws IllegalArgumentException 当 [ticks] 被传入小于或等于零的值
     */
    fun restart(ticks: Long) {
        reset()
        start(ticks)
    }

    /**
     * 重置冷却状态。
     *
     * 该方法是幂等的，多次调用不产生额外副作用。
     */
    fun reset() {
        bukkitTask?.cancel()
        bukkitTask = null
    }
}
package com.akira.core.api.util.general

import com.akira.core.api.AkiraPlugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

/**
 * 冷却管理器
 *
 * - 封装延时任务与状态管理
 * - 非线程安全
 *
 * 需提供一个 [AkiraPlugin] 作为任务归属。
 *
 * @property plugin 所属插件
 */
class Cooldown(val plugin: AkiraPlugin) {
    /**
     * 延时任务，用于延时重置冷却状态
     *
     * 自身亦可指示冷却状态，
     * 非 `null` 代表处于冷却状态。
     *
     * 当冷却自然结束或重置，将设为 `null`。
     */
    private var bukkitTask: BukkitTask? = null

    /**
     * 指示是否处于冷却状态
     */
    val inCooldown get() = bukkitTask != null

    /**
     * 启用冷却，并指定冷却时长。
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
     * 强制重启冷却。
     *
     * 封装了 [reset] 与 [start] 的逻辑。
     *
     * @param ticks 冷却时长，单位为游戏刻
     * @throws IllegalArgumentException 当 [ticks] 被传入小于或等于零的值
     */
    fun restart(ticks: Long) {
        reset()
        start(ticks)
    }

    /**
     * 重置冷却状态。此方法是幂等的。
     */
    fun reset() {
        bukkitTask?.cancel()
        bukkitTask = null
    }
}
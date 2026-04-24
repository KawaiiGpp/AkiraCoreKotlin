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
 * @property plugin 所属插件
 */
class Cooldown(val plugin: AkiraPlugin) {
    /**
     * 延时任务，用于延时重置冷却状态
     *
     * - 若不为 `null` 代表正在冷却
     * - 冷却自然结束或重置时，将设为 `null`
     */
    private var bukkitTask: BukkitTask? = null

    /**
     * 若正在冷却则为 `true`，否则为 `false`
     */
    val inCooldown get() = bukkitTask != null

    /**
     * 启用冷却，以 [ticks] 为时长，单位为游戏刻。
     *
     * @return 启用成功返回 `true`，`false` 表示已在冷却
     * @throws IllegalArgumentException 当 [ticks] 被传入小于或等于零的值时抛出
     */
    fun start(ticks: Long): Boolean {
        require(ticks > 0) { "Ticks for cooldown must be > 0, but actual: $ticks." }
        if (inCooldown) return false

        val scheduler = Bukkit.getScheduler()
        bukkitTask = scheduler.runTaskLater(plugin, { -> bukkitTask = null }, ticks)
        return true
    }

    /**
     * 强制重启冷却，以 [ticks] 为时长，单位为游戏刻。
     *
     * @throws IllegalArgumentException 当 [ticks] 被传入小于或等于零的值时抛出
     */
    fun restart(ticks: Long) {
        reset()
        start(ticks)
    }

    /**
     * 重置冷却状态，若未在冷却则不作处理。
     */
    fun reset() {
        bukkitTask?.cancel()
        bukkitTask = null
    }
}
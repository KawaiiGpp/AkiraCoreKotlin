package com.akira.core.api.util.world

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player

/**
 * 粒子效果预设包
 *
 * 整合各种原版播放粒子效果的方法，
 * 通过统一封装处理，实现一次定义到处播放。
 *
 * @param type 粒子类型
 * @param amount 粒子数量
 * @param offsetX X轴的偏移或扩散范围
 * @param offsetY Y轴的偏移或扩散范围
 * @param offsetZ Z轴的偏移或扩散范围
 * @param extra 粒子速度或亮度
 * @param data 粒子附加数据
 */
data class ParticlePack(
    val type: Particle,
    val amount: Int = 1,
    val offsetX: Double = 0.0,
    val offsetY: Double = 0.0,
    val offsetZ: Double = 0.0,
    val extra: Double = 0.0,
    val data: Any? = null
) {
    /**
     * 快捷创建一个粒子效果预设包。
     *
     * 当你需要 `X/Y/Z` 三轴的偏移或扩散范围一致，
     * 可优先使用该构造方法。
     *
     * @param particle 粒子类型
     * @param amount 粒子数量
     * @param offset 统一应用于xyz三轴的偏移或扩散范围
     * @param extra 粒子速度或亮度
     * @param data 粒子附加数据
     * @see ParticlePack
     */
    constructor(
        particle: Particle,
        amount: Int = 1,
        offset: Double = 0.0,
        extra: Double = 0.0,
        data: Any? = null
    ) : this(
        particle, amount, offset, offset, offset, extra, data
    )

    /**
     * 向指定玩家播放粒子效果。
     *
     * @param player 播放对象
     * @param location 播放位置，默认为 [Player.location]
     */
    fun play(player: Player, location: Location = player.location) =
        player.spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, extra, data)

    /**
     * 在世界的指定坐标广播粒子效果。
     *
     * @param location 广播位置
     * @throws NullPointerException 当位置所属世界未定义
     */
    fun broadcast(location: Location) =
        location.requiredWorld.spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, extra, data)
}
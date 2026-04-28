package com.akira.core.api.util.world

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player

/**
 * 粒子效果预设包
 *
 * @property type 粒子类型
 * @property amount 粒子数量
 * @property offsetX X轴的偏移或扩散范围
 * @property offsetY Y轴的偏移或扩散范围
 * @property offsetZ Z轴的偏移或扩散范围
 * @property extra 粒子速度或亮度
 * @property data 粒子附加数据
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
     * 创建 `X/Y/Z` 轴统一 [offset] 的实例。
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
     * 向 [player] 播放粒子效果。
     */
    fun play(player: Player, location: Location = player.location) =
        player.spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, extra, data)

    /**
     * 在 [location] 处广播粒子效果。
     *
     * @throws NullPointerException 当位置所属世界未定义
     */
    fun broadcast(location: Location) =
        location.requiredWorld.spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, extra, data)
}
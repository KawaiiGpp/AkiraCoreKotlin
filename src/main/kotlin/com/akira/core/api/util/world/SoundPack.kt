package com.akira.core.api.util.world

import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * 声音预设包
 *
 * @property type 声音类型
 * @property pitch 声调。范围 `0.5-2.0`
 * @property volume 音量。范围 `>=0.0`
 */
data class SoundPack(
    val type: Sound,
    val pitch: Float = 1.0F,
    val volume: Float = 0.5F
) {
    /**
     * 单独向 [player] 播放声音，并随玩家移动。
     */
    fun play(player: Player) = player.playSound(player, type, volume, pitch)

    /**
     * 单独向 [player] 播放声音，不随玩家移动。
     */
    fun playEnvironment(player: Player) = player.playSound(player.location, type, volume, pitch)

    /**
     * 在 [entity] 处广播声音，并随实体移动。
     */
    fun broadcast(entity: Entity) = entity.world.playSound(entity, type, volume, pitch)

    /**
     * 在 [entity] 处广播声音，不随实体移动。
     */
    fun broadcastEnvironment(entity: Entity) = entity.world.playSound(entity.location, type, volume, pitch)

    /**
     * 在 [location] 处广播声音。
     *
     * @throws NullPointerException 当位置所属世界未定义
     */
    fun broadcast(location: Location) = location.requiredWorld.playSound(location, type, volume, pitch)
}
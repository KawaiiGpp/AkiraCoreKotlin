package com.akira.core.api.util.world

import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * 声音预设包
 *
 * 整合原版五花八门的声音播放方法，
 * 实现一次定义，到处播放。
 *
 * @property type 声音类型
 * @property pitch 声调。范围 `0.5-2.0`
 * @property volume 音量。范围 `0.0-1.0`，`>1.0` 时，可听见的范围将会增加，距离更远也能听见。
 */
data class SoundPack(
    val type: Sound,
    val pitch: Float = 1.0F,
    val volume: Float = 0.5F
) {
    /**
     * 以最原始的方式向玩家播放声音。
     * 无空间感，同时发声位置随玩家移动。
     *
     * 只有被选中的玩家能听到。
     *
     * @param player 播放对象
     */
    fun play(player: Player) = player.playSound(player, type, volume, pitch)

    /**
     * 以环境音的方式向玩家播放声音。
     * 有空间感，同时发声位置不随玩家变化。
     *
     * 只有被选中的玩家能听到。
     *
     * @param player 播放对象
     */
    fun playEnvironment(player: Player) = player.playSound(player.location, type, volume, pitch)

    /**
     * 以指定实体为发声位置，并随实体移动。
     * 会广播声音，附近的玩家能听到。
     *
     * @param entity 播放对象
     */
    fun broadcast(entity: Entity) = entity.world.playSound(entity, type, volume, pitch)

    /**
     * 以指定实体为发声位置，不随实体移动。
     * 会广播声音，附近的玩家能听到。
     *
     * @param entity 播放对象
     */
    fun broadcastEnvironment(entity: Entity) = entity.world.playSound(entity.location, type, volume, pitch)

    /**
     * 在指定位置广播声音。
     *
     * @param location 发声位置
     * @throws NullPointerException 当位置所属世界未定义
     */
    fun broadcast(location: Location) = location.requiredWorld.playSound(location, type, volume, pitch)
}
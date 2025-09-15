package com.akira.core.api.util.world

import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

data class SoundPack(
    val type: Sound,
    val pitch: Float = 1.0F,
    val volume: Float = 0.5F
) {
    fun play(player: Player) = player.playSound(player, type, volume, pitch)

    fun playEnvironment(player: Player) = player.playSound(player.location, type, volume, pitch)

    fun broadcast(entity: Entity) = entity.world.playSound(entity, type, volume, pitch)

    fun broadcastEnvironment(entity: Entity) = entity.world.playSound(entity.location, type, volume, pitch)

    fun broadcast(location: Location) = location.worldNonNull.playSound(location, type, volume, pitch)
}
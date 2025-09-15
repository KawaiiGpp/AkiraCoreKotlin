package com.akira.core.api.util.world

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player

data class ParticlePack(
    val type: Particle,
    val amount: Int = 1,
    val offsetX: Double = 0.0,
    val offsetY: Double = 0.0,
    val offsetZ: Double = 0.0,
    val extra: Double = 0.0,
    val data: Any? = null
) {
    constructor(
        particle: Particle,
        amount: Int = 1,
        offset: Double = 0.0,
        extra: Double = 0.0,
        data: Any? = null
    ) : this(
        particle, amount,
        offset, offset, offset,
        extra, data
    )

    fun play(player: Player, location: Location = player.location) =
        player.spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, extra, data)

    fun broadcast(location: Location) =
        location.worldNonNull.spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, extra, data)
}
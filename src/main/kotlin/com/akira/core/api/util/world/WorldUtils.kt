package com.akira.core.api.util.world

import com.akira.core.api.util.math.requiresLegit
import org.bukkit.Bukkit
import org.bukkit.Location

fun serializeLocation(target: Location): String =
    "${target.worldNonNull.name}~${target.x},${target.y},${target.z}~${target.yaw},${target.pitch}"

fun deserializeLocation(raw: String): Location {
    try {
        val main = raw.split('~')
        require(main.size == 3) { "Incorrect length: ${main.size}" }


        val world = Bukkit.getWorld(main[0])
        require(world != null) { "World ${main[0]} not found." }

        val point = main[1].split(',')
        require(point.size == 3) { "Incorrect XYZ section length: ${point.size}" }
        val x = point[0].toDouble().requiresLegit()
        val y = point[1].toDouble().requiresLegit()
        val z = point[2].toDouble().requiresLegit()

        val dir = main[2].split(',')
        require(dir.size == 2) { "Incorrect direction section length: ${dir.size}" }
        val yaw = dir[0].toFloat().requiresLegit()
        val pitch = dir[1].toFloat().requiresLegit()


        return Location(world, x, y, z, yaw, pitch)
    } catch (exception: Throwable) {
        throw IllegalArgumentException("Failed parsing raw location: $raw", exception)
    }
}

fun deserializeLocationNullable(raw: String): Location? {
    return try {
        deserializeLocation(raw)
    } catch (exception: Exception) {
        null
    }
}
package com.akira.core.api.util.world

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

/**
 * 获取一个已通过空检查的 [Location.world]。
 *
 * @throws NullPointerException 当前 [Location] 对象未定义所属世界
 */
val Location.requiredWorld: World
    get() {
        if (this.world != null) return this.world

        val xyz = "x=$x, y=$y, z=$z, yaw=$yaw, pitch=$pitch"
        val message = "World not defined in this location instance: "
        throw NullPointerException("$message $xyz")
    }

/**
 * 将位置实例简化为紧凑的单行字符串。
 *
 * 适用于轻量化存储或临时控制台输出。
 * * 格式：`世界`~`X,Y,Z`~`Yaw/Pitch`
 * * 注意：转换回 [Location] 需用 [String.toLocation]。
 *
 * 需确保 [Location.world] 不为 `null`，
 * 否则世界将保存为字符串的 `"null"`，且解析回 [Location] 时会失败。
 *
 * @return 简化为单行字符串的位置信息
 * @see String.toLocation
 */
fun Location.toStringSingleLine(): String {
    val worldName = this.world?.name ?: "null"
    return "$worldName~$x,$y,$z~$yaw,$pitch"
}

/**
 * 将符合 [Location.toStringSingleLine]
 * 产出格式的字符串转换回 [Location] 对象。
 *
 * @return 解析结果，若失败则返回 `null`
 * @see Location.toStringSingleLine
 */
fun String.toLocation(): Location? {
    return runCatching {
        val raw = split("~")
        if (raw.size != 3) return null

        val worldName = raw[0].takeIf { it != "null" } ?: return null
        val world = Bukkit.getWorld(worldName) ?: return null

        val coords = raw[1].split(",")
        if (coords.size != 3) return null
        val (x, y, z) = coords.map { it.toDouble() }

        val dir = raw[2].split(",")
        if (dir.size != 2) return null
        val (yaw, pitch) = dir.map { it.toFloat() }

        return Location(world, x, y, z, yaw, pitch)
    }.getOrNull()
}
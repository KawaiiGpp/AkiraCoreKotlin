package com.akira.core.api.util.world

import com.akira.core.api.util.general.nullPointer
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
        nullPointer("World not defined in this location instance: $xyz.")
    }

/**
 * 将位置实例简化为紧凑的单行字符串。
 *
 * - 格式：`世界`~`X,Y,Z`~`Yaw/Pitch`
 * - 逆操作请使用 [String.toLocation]
 *
 * @throws NullPointerException 当位置所属世界未定义
 */
fun Location.toStringSingleLine(): String = "${requiredWorld.name}~$x,$y,$z~$yaw,$pitch"

/**
 * 将符合格式的字符串解析为 [Location] 对象。
 *
 * @return 解析结果，失败则返回 `null`
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
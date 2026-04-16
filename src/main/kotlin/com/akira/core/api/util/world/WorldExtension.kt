package com.akira.core.api.util.world

import org.bukkit.Location
import org.bukkit.World

/**
 * 获取一个已通过空检查的 [Location.world]。
 *
 * @return 当前 [Location] 所属的、非空的世界实例
 * @throws NullPointerException 当前 [Location] 对象未定义所属世界
 */
val Location.requiredWorld: World get() {
    if (this.world != null) return this.world

    val xyz = "x=$x, y=$y, z=$z, yaw=$yaw, pitch=$pitch"
    val message = "World not defined in this location instance: "
    throw NullPointerException("$message $xyz")
}
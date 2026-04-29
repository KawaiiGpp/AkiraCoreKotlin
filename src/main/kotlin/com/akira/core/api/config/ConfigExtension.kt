package com.akira.core.api.config

import com.akira.core.api.util.general.illegalState
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection

/**
 * 根据路径读取世界名并解析成 [World] 实例。
 *
 * - 路径不存在、或存在但世界未加载时返回 `null`
 */
fun ConfigurationSection.getWorldByName(path: String): World? {
    val name = this.getString(path) ?: return null
    return Bukkit.getWorld(name)
}

/**
 * 根据路径读取集合，并将每个元素解析为 [Location] 实例。
 *
 * - 若路径不存在，则返回空列表
 * - 无法解析或为 `null` 的元素将从列表中过滤
 */
fun ConfigurationSection.getLocationList(path: String): List<Location> {
    val section = this.getConfigurationSection(path) ?: return listOf()
    return section.getKeys(false).mapNotNull(section::getLocation)
}

/**
 * 清空当前节点下的所有子节点。
 *
 * - 非递归，等效于将每个键设置为 `null`
 */
fun ConfigurationSection.clear() = this.getKeys(false).forEach { this.set(it, null) }

/**
 * 断言路径所对应节点存在并获取。
 *
 * @throws IllegalStateException 当路径不存在
 */
fun ConfigurationSection.requireSection(path: String): ConfigurationSection {
    return this.getConfigurationSection(path) ?: illegalState("Section $path not existing.")
}

/**
 * 获取路径对应的节点，若路径不存在则自动创建。
 */
fun ConfigurationSection.getOrCreate(path: String) = this.getConfigurationSection(path) ?: createSection(path)
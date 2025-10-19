package com.akira.core.api.config

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection

fun ConfigurationSection.getWorld(path: String): World? {
    val name = this.getString(path) ?: return null
    return Bukkit.getWorld(name)
}

fun ConfigurationSection.getLocationList(path: String): List<Location>? {
    val section = this.getConfigurationSection(path) ?: return null
    return section.getKeys(false).mapNotNull(section::getLocation)
}

fun <T : Any> ConfigurationSection.getSerializable(path: String, serializer: ConfigSerializer<T>): T? =
    this.getConfigurationSection(path)?.let { serializer.deserialize(it) }

fun <T : Any> ConfigurationSection.setSerializable(path: String, value: T, serializer: ConfigSerializer<T>) =
    serializer.serialize(value, this.getConfigurationSection(path) ?: createSection(path))

fun ConfigurationSection.clear() = this.getKeys(false).forEach { this.set(it, null) }

fun ConfigurationSection.getNonNullSection(path: String) =
    requireNotNull(this.getConfigurationSection(path)) { "Section $path not existing." }

fun ConfigurationSection.getOrCreate(path: String) = this.getConfigurationSection(path) ?: createSection(path)
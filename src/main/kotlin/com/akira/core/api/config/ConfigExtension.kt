package com.akira.core.api.config

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration

fun YamlConfiguration.getWorld(path: String): World? {
    val name = this.getString(path) ?: return null
    return Bukkit.getWorld(name)
}

fun YamlConfiguration.getLocationList(path: String): List<Location>? {
    val section = this.getConfigurationSection(path) ?: return null
    return section.getKeys(false).mapNotNull(section::getLocation)
}
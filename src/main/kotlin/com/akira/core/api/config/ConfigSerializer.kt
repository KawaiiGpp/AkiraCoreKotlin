package com.akira.core.api.config

import org.bukkit.configuration.ConfigurationSection

interface ConfigSerializer<T : Any> {
    fun serialize(value: T, section: ConfigurationSection)

    fun deserialize(section: ConfigurationSection): T?
}
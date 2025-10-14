package com.akira.core.api.config

import org.bukkit.configuration.ConfigurationSection

interface ConfigSerializable {
    fun serialize(section: ConfigurationSection)

    fun deserialize(section: ConfigurationSection)
}
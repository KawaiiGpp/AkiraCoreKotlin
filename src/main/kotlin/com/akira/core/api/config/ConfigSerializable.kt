package com.akira.core.api.config

import org.bukkit.configuration.ConfigurationSection

interface ConfigSerializable<T> {
    fun serialize(value: T, config: ConfigurationSection)

    fun deserialize(config: ConfigurationSection): T
}
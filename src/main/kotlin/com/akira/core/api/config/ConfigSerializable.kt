package com.akira.core.api.config

import org.bukkit.configuration.ConfigurationSection

interface ConfigSerializable<T : Any> {
    fun serialize(section: ConfigurationSection)

    fun deserialize(section: ConfigurationSection): T?
}
package com.akira.core.api.config

import com.akira.core.api.EnhancedManager

class ConfigManager : EnhancedManager<ConfigFile>() {
    fun initializeAll() = registry.values.forEach(ConfigFile::initialize)

    fun saveAll() = registry.values.forEach(ConfigFile::save)

    override fun transform(element: ConfigFile): String = element.name
}
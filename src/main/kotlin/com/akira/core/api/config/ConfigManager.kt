package com.akira.core.api.config

import com.akira.core.api.Manager

class ConfigManager : Manager<String, ConfigFile>() {
    fun initializeAll() = map.values.forEach(ConfigFile::initialize)

    fun saveAll() = map.values.forEach(ConfigFile::save)
}
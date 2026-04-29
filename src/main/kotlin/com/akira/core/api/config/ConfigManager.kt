package com.akira.core.api.config

import com.akira.core.api.Manager

/**
 * 配置文件管理器
 *
 * - 对已注册配置文件进行批量操作
 */
class ConfigManager : Manager<ConfigFile>() {
    fun initializeAll() = registry.values.forEach(ConfigFile::initialize)

    fun saveAll() = registry.values.forEach(ConfigFile::save)

    override fun transform(element: ConfigFile): String = element.name
}
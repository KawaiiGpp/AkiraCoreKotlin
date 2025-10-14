package com.akira.core.api.config

import com.akira.core.api.AkiraPlugin
import org.bukkit.configuration.file.YamlConfiguration
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

abstract class ConfigFile(
    protected val plugin: AkiraPlugin,
    protected val name: String,
    protected val templatePath: String? = null
) {
    protected val fullName = "$name.yml"
    protected val file = File(plugin.dataFolder, fullName)
    protected lateinit var config: YamlConfiguration

    fun save() = config.save(file)

    fun load() {
        config = YamlConfiguration.loadConfiguration(file)
        if (templatePath != null) this.applyTemplate()
    }

    fun initialize() {
        plugin.dataFolder.mkdirs()

        if (!file.exists())
            file.createNewFile()

        this.load()
    }

    private fun applyTemplate() {
        val stream = plugin.getResource("$templatePath/$fullName")
        requireNotNull(stream) { "Cannot find the template for $name at $templatePath." }

        BufferedReader(InputStreamReader(stream, Charsets.UTF_8)).use {
            config.addDefaults(YamlConfiguration.loadConfiguration(it))
            config.options().copyDefaults(true)
        }
    }
}
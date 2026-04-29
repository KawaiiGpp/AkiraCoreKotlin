package com.akira.core.api.config

import com.akira.core.api.AkiraPlugin
import com.akira.core.api.util.general.illegalState
import org.bukkit.configuration.file.YamlConfiguration
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * 配置文件抽象基类
 *
 * - 从磁盘中加载，或从内存储存到磁盘
 * - 可设定预设模板，在加载时套用
 *
 * @property name 配置名
 * @property fileName 磁盘中的文件命名
 * @param plugin 所属插件
 * @param templatePath 预设模板的资源路径
 */
abstract class ConfigFile(
    val name: String,
    protected val plugin: AkiraPlugin,
    protected val templatePath: String? = null
) {
    val fileName = "$name.yml"

    /**
     * 磁盘中的文件引用
     */
    protected val file = File(plugin.dataFolder, fileName)

    /**
     * 被加载到内存中的配置实例
     */
    protected lateinit var config: YamlConfiguration

    /**
     * 把内存中的配置实例保存到磁盘。
     */
    fun save() = config.save(file)

    /**
     * 从磁盘中加载配置文件到内存。
     *
     * - 若已配置预设模板，则自动应用
     */
    fun load() {
        config = YamlConfiguration.loadConfiguration(file)
        if (templatePath != null) this.applyTemplate()
    }

    /**
     * 初始化配置实例。
     *
     * - 在磁盘创建文件以及所需文件夹
     * - 自动把配置文件内容加载到内存中
     * - 若已配置预设模板则自动应用
     */
    fun initialize() {
        plugin.dataFolder.mkdirs()

        if (!file.exists())
            file.createNewFile()

        this.load()
    }

    /**
     * 应用预设模板到当前配置。
     *
     * @throws IllegalStateException 预设模板缺失
     */
    private fun applyTemplate() {
        val stream = plugin.getResource("$templatePath/$fileName")
            ?: illegalState("Cannot find the template for $name at $templatePath.")

        BufferedReader(InputStreamReader(stream, Charsets.UTF_8))
            .use {
                config.addDefaults(YamlConfiguration.loadConfiguration(it))
                config.options().copyDefaults(true)
            }
    }
}
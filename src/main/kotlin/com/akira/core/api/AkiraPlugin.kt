package com.akira.core.api

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

abstract class AkiraPlugin : JavaPlugin() {
    override fun onEnable() {
        val version = pluginMeta.version;
        val authors = pluginMeta.authors.joinToString(", ")

        logInfo("插件 $name 已成功启用。")
        logInfo("版本：$version，作者：$authors")
    }

    override fun onLoad() = logInfo("欢迎使用 $name，插件正在加载。")

    override fun onDisable() = logInfo("感谢使用 $name，期待下次再见。")

    fun logInfo(message: String) = log("Info", NamedTextColor.GREEN, message)

    fun logWarn(message: String) = log("Warn", NamedTextColor.YELLOW, message)

    fun logError(message: String) = log("Error", NamedTextColor.RED, message)

    private fun log(prefix: String, color: TextColor, message: String) {
        val tag = Component.text("[", NamedTextColor.GRAY)
            .append(Component.text(name, color))
            .append(Component.text("] [", NamedTextColor.GRAY))
            .append(Component.text(prefix, color))
            .append(Component.text("] ", NamedTextColor.GRAY))

        val message = tag.append(Component.text(message, color))
        Bukkit.getConsoleSender().sendMessage(message)
    }
}
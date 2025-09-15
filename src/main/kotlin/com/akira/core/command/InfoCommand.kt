package com.akira.core.command

import com.akira.core.AkiraCore
import com.akira.core.api.command.CommandNode
import com.akira.core.api.command.EnhancedExecutor
import com.akira.core.api.command.SenderLimit
import com.akira.core.api.util.text.sendLine
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender

class InfoCommand(plugin: AkiraCore) : EnhancedExecutor(plugin, "akiracore") {
    init {
        registerNode(Info())
    }

    private inner class Info : CommandNode(name, SenderLimit.NONE, arrayOf(), "显示当前插件信息。") {
        override fun execute(sender: CommandSender, args: Array<String>): Boolean {
            sender.sendLine(45, NamedTextColor.DARK_GRAY)

            sender.sendMessage(
                Component.text("Akira Core (Kotlin ver.) ", NamedTextColor.LIGHT_PURPLE)
                    .append(Component.text("v${plugin.pluginMeta.version}", NamedTextColor.WHITE))
            )

            sender.sendMessage(
                Component.text("开发者：", NamedTextColor.GRAY)
                    .append(Component.text(plugin.pluginMeta.authors.joinToString(), NamedTextColor.GREEN))
            )

            sender.sendMessage(Component.text("插件已在正常工作，感谢你的选择！", NamedTextColor.GRAY))
            sender.sendLine(45, NamedTextColor.DARK_GRAY)
            return true
        }
    }
}
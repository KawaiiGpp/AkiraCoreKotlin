package com.akira.core.api.command

import com.akira.core.api.AkiraPlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

abstract class EnhancedExecutor(protected val plugin: AkiraPlugin, val name: String) : TabExecutor {
    private val handler: CommandHandler = CommandHandler(plugin, name)

    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<String>
    ): Boolean = handler.execute(sender, args).let { true }

    override fun onTabComplete(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<String>
    ): List<String?>? = handler.suggest(sender, args).ifEmpty { null }

    protected fun registerNode(node: CommandNode) = handler.register(node)
}
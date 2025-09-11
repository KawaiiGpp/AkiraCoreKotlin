package com.akira.core.api.command

import com.akira.core.api.AkiraPlugin
import com.akira.core.api.util.text.sendEmptyLine
import com.akira.core.api.util.text.sendLine
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender

class CommandHandler(private val plugin: AkiraPlugin, val name: String) {
    private val nodes: MutableSet<CommandNode> = mutableSetOf()
    val registeredNodes get() = nodes.toSet()

    init {
        register(this.createHelpCommandNode())
    }

    fun register(node: CommandNode) {
        require(!nodes.contains(node)) { "Node ${node.asString} already registered." }

        nodes.forEach {
            require(!it.conflictsWith(node)) { "Node ${node.asString} conflicts with existing node ${it.asString}." }
        }

        nodes.add(node)
    }

    fun execute(sender: CommandSender, args: Array<String>) {
        val node = nodes.filter { it.matches(args) }
            .maxByOrNull { it.arguments.count { it.literal } }

        when {
            node == null -> sender.sendMessage(
                Component.text(
                    "未知指令，请使用 /$name help 查看可用指令。",
                    NamedTextColor.RED
                )
            )

            !node.limit.allow(sender) -> sender.sendMessage(
                Component.text(
                    "错误：${node.limit.deniedMessage}",
                    NamedTextColor.RED
                )
            )

            else -> {
                runCatching { node.executeCommand(sender, args) }
                    .onFailure {
                        sender.sendMessage(
                            Component.text(
                                "执行指令时发生错误，请联系开发者。",
                                NamedTextColor.DARK_RED
                            )
                        )

                        plugin.logError("执行指令 /$name 时发生异常。")
                        plugin.logError("节点：${node.asString}")

                        it.printStackTrace()
                    }
                    .onSuccess {
                        if (!it) sender.sendMessage(
                            Component.text(
                                "语法错误，用法：${node.usage}",
                                NamedTextColor.RED
                            )
                        )
                    }
            }
        }
    }

    fun suggest(sender: CommandSender, args: Array<String>): List<String> =
        nodes.filter { it.shouldSuggest(args) }
            .filter { it.limit.allow(sender) }
            .map { it.arguments[args.lastIndex] }
            .filter { it.literal }
            .map { it.content }.toList()

    private fun createHelpCommandNode(): CommandNode =
        object : CommandNode(name, SenderLimit.NONE, arrayOf("help"), "列出可用的所有指令节点。") {
            override fun execute(sender: CommandSender, args: Array<String>): Boolean {
                sender.sendLine(60, NamedTextColor.DARK_GRAY)

                sender.sendMessage(
                    Component.text("以下为 ", NamedTextColor.WHITE)
                        .append(Component.text("/$name", NamedTextColor.YELLOW))
                        .append(Component.text(" 的指令节点：", NamedTextColor.WHITE))
                )
                sender.sendEmptyLine()

                nodes.flatMap {
                    listOf(
                        Component.text("- ", NamedTextColor.DARK_GRAY)
                            .append(Component.text(it.usage, NamedTextColor.DARK_GREEN)),
                        Component.text("- >> ", NamedTextColor.DARK_GRAY)
                            .append(Component.text(it.description, NamedTextColor.GRAY))
                    )
                }.forEach(sender::sendMessage)

                sender.sendLine(60, NamedTextColor.DARK_GRAY)
                return true
            }
        }
}
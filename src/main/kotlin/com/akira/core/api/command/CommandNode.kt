package com.akira.core.api.command

import org.bukkit.command.CommandSender

abstract class CommandNode(
    val root: String,
    val limit: SenderLimit,
    val arguments: List<CommandArg>,
    val description: String
) {
    val asString = arguments.joinToString(" ") { it.formatted }
    val usage = "/$root $asString"

    constructor(root: String, limit: SenderLimit, arguments: Array<String>, description: String) :
            this(root, limit, arguments.map { CommandArg.forRaw(it) }, description)

    fun executeCommand(sender: CommandSender, args: Array<String>): Boolean {
        require(args.size == arguments.size) { "Incorrect number of arguments." }

        val filtered = arguments.indices.filter { arguments[it].param }.map { args[it] }.toTypedArray()
        return execute(sender, filtered)
    }

    fun conflictsWith(commandNode: CommandNode): Boolean =
        arguments.size == commandNode.arguments.size
                && arguments.withIndex().all { (i, arg) -> arg.conflictsWith(commandNode.arguments[i]) }

    fun matches(args: Array<String>): Boolean {
        if (args.size != arguments.size)
            return false

        return arguments.indices
            .filter { arguments[it].literal }
            .all { arguments[it].content == args[it] }
    }

    fun shouldSuggest(args: Array<String>): Boolean {
        if (args.size > arguments.size)
            return false

        return args.withIndex().all { (index, arg) ->
            val argument = arguments[index]

            argument.param || if (index != args.lastIndex)
                argument.content == arg
            else argument.content.startsWith(arg)
        }
    }

    protected abstract fun execute(sender: CommandSender, args: Array<String>): Boolean
}
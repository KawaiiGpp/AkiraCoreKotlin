package com.akira.core.api.command

class CommandArg(val content: String, val literal: Boolean = true) {
    val formatted = if (literal) content else "<$content>"
    val param = !literal

    init {
        require(!content.contains(Regex("\\s"))) { "Content of command arg cannot contain any space." }
        require(!content.isBlank()) { "Content of command arg cannot be blank." }
    }

    fun conflictsWith(that: CommandArg): Boolean = when {
        literal != that.literal -> false
        !literal -> true
        else -> content == that.content
    }

    companion object {
        fun forRaw(raw: String): CommandArg {
            val hasPrefix = raw.startsWith('#')

            return if (!hasPrefix) CommandArg(raw, true)
            else CommandArg(raw.substring(1), false)
        }
    }
}
package com.akira.core.api.command

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KClass

enum class SenderLimit(val senderType: KClass<*>, val deniedMessage: String) {
    NONE(Any::class, "对执行者类型没有限制。"),
    CONSOLE(ConsoleCommandSender::class, "只能由控制台执行。"),
    PLAYER(Player::class, "只能由玩家执行。");

    fun allow(sender: CommandSender): Boolean = senderType.isInstance(sender)
}
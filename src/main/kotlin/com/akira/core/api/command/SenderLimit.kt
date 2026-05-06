package com.akira.core.api.command

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KClass

/**
 * 执行者身份限制
 *
 * - 指示执行指令所需的身份
 * - 封装身份验证的逻辑与拒绝执行的反馈信息
 *
 * @property senderType 允许的执行者类型
 * @property deniedMessage 验证失败的反馈信息
 */
enum class SenderLimit(val senderType: KClass<*>, val deniedMessage: String) {
    NONE(Any::class, "对执行者类型没有限制。"),
    CONSOLE(ConsoleCommandSender::class, "只能由控制台执行。"),
    PLAYER(Player::class, "只能由玩家执行。");

    /**
     * 验证指令执行者是否通过身份验证。
     */
    fun allow(sender: CommandSender): Boolean = senderType.isInstance(sender)
}
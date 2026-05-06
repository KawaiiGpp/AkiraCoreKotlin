package com.akira.core.api.command

import com.akira.core.api.AkiraPlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

/**
 * 增强指令执行器
 * - 用于将整套指令处理系统接入 `Bukkit API`
 * - 内部维护 [CommandHandler] 用于对用户输入进行识别分发
 *
 * 使用方式：
 * - 调用 [registerRoute] 注册指令路径用于识别分发
 * - 调用 [AkiraPlugin.setupCommand] 向 `Bukkit` 注册执行器
 *
 * @property name 指令名，不含 `/`
 * @param plugin 所属插件
 */
abstract class EnhancedExecutor(protected val plugin: AkiraPlugin, val name: String) : TabExecutor {
    private val handler: CommandHandler = CommandHandler(plugin, name)

    override fun onCommand(sender: CommandSender, cmd: Command, text: String, args: Array<String>): Boolean {
        handler.execute(sender, args)
        return true
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, text: String, args: Array<String>): List<String?>? {
        val result = handler.suggest(sender, args)
        return result.ifEmpty { null }
    }

    protected fun registerRoute(route: CommandRoute) = handler.register(route)
}
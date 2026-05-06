package com.akira.core.api.command

import com.akira.core.api.AkiraPlugin
import com.akira.core.api.util.text.sendEmptyLine
import com.akira.core.api.util.text.sendLine
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender
import java.util.*

/**
 * 指令分发器
 *
 * - 负责指令的路径储存与事件分发
 *
 * @param plugin 所属插件
 *
 * @property name 指令名，不含 `/`
 * @property routes 所储存路径的只读视图
 */
class CommandHandler(private val plugin: AkiraPlugin, val name: String) {
    private val storage: MutableSet<CommandRoute> = mutableSetOf()
    val routes: Set<CommandRoute> get() = Collections.unmodifiableSet(storage)

    init {
        register(HelpCommandRoute()) // 强制自动注册，还顺便防止了指令路径真空的情况。
    }

    /**
     * 注册指令路径。
     *
     * @throws IllegalArgumentException 当相同路径已注册，或与其他已注册路径存在冲突时抛出
     */
    fun register(route: CommandRoute) {
        require(!storage.contains(route)) {
            "From /$name: Route '${route.asString}' already exists."
        }

        storage.forEach {
            require(!it.conflictsWith(route)) {
                "From /$name: Route '${route.asString}' conflicts with an existing route '${it.asString}'."
            }
        }

        storage.add(route)
    }

    /**
     * 验证身份，然后分发指令并执行。
     *
     * - 拦截未注册指令
     * - 对 [sender] 进行身份验证
     * - 根据 [args] 筛选路径并传入执行
     */
    fun execute(sender: CommandSender, args: Array<String>) {
        // 筛选匹配的路径，结果可能有多个，
        // 原则为精确者胜出，即匹配的字面值元素最多者。
        // result[0] - /skills set <player> skill <level> 优先选这个
        // result[1] - /skills set <player> <attribute> <value>
        // 当字面值元素存在相同情况时（极端情况），则选取首个。
        // reuslt[0] - /skills reset <player> <skill_type> 优先选这个
        // result[1] - /skills <operation> <player> -silent
        val route = storage
            .filter { it.matches(args) }
            .maxByOrNull { it.elements.count { it.isLiteral } }

        when {
            route == null -> sender.sendMessage(
                Component.text(
                    "未知指令，请使用 /$name help 查看可用指令。",
                    NamedTextColor.RED
                )
            )

            !route.limit.allow(sender) -> sender.sendMessage(
                Component.text(
                    "无法以当前身份执行：${route.limit.deniedMessage}",
                    NamedTextColor.RED
                )
            )

            else -> this.dispatch(sender, args, route)
        }
    }

    /**
     * 根据用户实时输入提供路径建议。
     *
     * - 如果身份不符合要求，则不予提示
     * - 权限相关的拦截由 `plugin.yml` 的配置提供
     */
    fun suggest(sender: CommandSender, args: Array<String>): List<String> {
        return storage
            .filter { it.shouldSuggest(args) } // 初筛，同时确保路径长度必然大于等于实际输入长度
            .filter { it.limit.allow(sender) } // 过滤身份要求不通过的
            .map { it.elements[args.lastIndex] } // 抽取用户正在输入的那一项
            .filter { it.isLiteral } // 只有字面值有提示价值
            .map { it.name } // 映射成具体内容
    }

    /**
     * 分发用户输入给指定指令路径。
     *
     * 分离 [execute] 分发部分的逻辑，还封装了：
     * - 捕获指令执行过程中的异常
     * - 根据需要，展示指令用法提示
     */
    private fun dispatch(sender: CommandSender, args: Array<String>, route: CommandRoute) {
        runCatching { route.executeCommand(sender, args) }.fold(
            onFailure = { throwable ->
                sender.sendMessage(Component.text("指令执行发生错误，请联系开发者。", NamedTextColor.DARK_RED))

                plugin.logError("执行指令 /$name 时发生异常。")
                plugin.logError("路径：${route.asString}")

                throwable.printStackTrace()
            },

            onSuccess = { result ->
                if (result) return

                sender.sendMessage(Component.text("语法错误，用法：${route.usage}", NamedTextColor.RED))
            }
        )
    }

    /**
     * 帮助指令
     *
     * - 自动注册，用于向用户展示指令的所有可用路径
     * - 该路径内容为：`/<command_name> help`
     */
    private inner class HelpCommandRoute : CommandRoute(
        name, SenderLimit.NONE, arrayOf("help"), "列出可用的所有指令路径。"
    ) {
        override fun execute(sender: CommandSender, args: Array<String>): Boolean {
            sender.sendLine(60, NamedTextColor.DARK_GRAY)

            sender.sendMessage(
                Component.text("以下为 ", NamedTextColor.WHITE)
                    .append(Component.text("/$name", NamedTextColor.YELLOW))
                    .append(Component.text(" 的指令路径：", NamedTextColor.WHITE))
            )
            sender.sendEmptyLine()

            storage.flatMap {
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
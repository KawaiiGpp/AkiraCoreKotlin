package com.akira.core.api.command

import org.bukkit.command.CommandSender

/**
 * 指令路径
 *
 * - 可被识别并执行的指令分支，代表一条子指令
 * - 封装了身份要求等属性，以及执行逻辑
 *
 * @property root 主指令名，不含 `/`
 * @property limit 执行者身份限制
 * @property elements 构成路径的元素序列
 * @property description 功能描述
 */
abstract class CommandRoute(
    val root: String,
    val limit: SenderLimit,
    val elements: List<CommandElement>,
    val description: String
) {
    /**
     * 路径的字符串形式
     */
    val asString = elements.joinToString(" ") { it.displayName }

    /**
     * 路径的用法展示
     */
    val usage = "/$root $asString"

    /**
     * 通过字符串数组直接定义指令路径。
     *
     * - 使用 [CommandElement.parse] 解析字符串
     */
    constructor(root: String, limit: SenderLimit, arguments: Array<String>, description: String) :
            this(root, limit, arguments.map(CommandElement::parse), description)

    /**
     * 分发指令到该指令路径。
     * - 精简用户输入全文，仅保留用户动态输入的部分，再传入 [execute] 触发该路径定义的执行逻辑
     *
     * 示例：
     * - `worldspawn set` → `[]`
     * - `set <player> <level>` → `["akira", "12"]`
     * - `reset <player>` → `["soda_tea"]`
     */
    fun executeCommand(sender: CommandSender, args: Array<String>): Boolean {
        require(args.size == elements.size) {
            "Incorrect size of arguments. ${elements.size} required, but actual: ${args.size}."
        }

        val filtered = elements.indices
            .filter { elements[it].isVariable } // 只要动态值的索引
            .map { args[it] } // 用动态值索引，从用户输入里拿实参
            .toTypedArray()

        return execute(sender, filtered)
    }

    /**
     * 指令路径冲突检测。
     * - 判断该路径与另一路径是否无法区分
     *
     * 以下路径，在分发时将无法区分：
     * - `level set <value>`
     * - `level set <player>`
     */
    fun conflictsWith(route: CommandRoute): Boolean {
        if (elements.size != route.elements.size) return false

        // 一旦每个元素都冲突（无法区分），那么这两条指令路径将彻底无法区分。
        return elements.indices.all { elements[it].conflictsWith(route.elements[it]) }
    }

    /**
     * 判断用户输入是否与当前指令路径匹配。
     *
     * - 逐一比对二者的字面值元素，全部匹配则返回 `true`
     */
    fun matches(args: Array<String>): Boolean {
        if (args.size != elements.size) return false

        // 只对比字面值部分，所有字面值元素内容一致则算匹配。
        return elements.indices
            .filter { elements[it].isLiteral }
            .all { elements[it].name == args[it] }
    }

    /**
     * 根据用户输入，判断是否需要建议该指令路径。
     */
    fun shouldSuggest(args: Array<String>): Boolean {
        // 注：用户实际输入以下称”实参“，指令路径元素以下称”形参“。
        // 实参数量只有小于等于形参总数，才有建议补全的意义。
        if (args.size > elements.size) return false

        // 判断每个实参是否均与每个形参相匹配。
        return args.withIndex().all { (index, arg) ->
            val element = elements[index]

            // 该形参作用为获取用户输入，无比较价值。
            if (element.isVariable) return true

            // 对比每个实参内容是否与形参匹配，
            // 对于最后一个实参，则对比已输入部分是否与形参匹配。
            return if (index != args.lastIndex) element.name == arg
            else element.name.startsWith(arg)
        }
    }

    /**
     * 该路径的执行逻辑。
     *
     * @return 返回 `false` 时将自动提示指令用法给用户
     */
    protected abstract fun execute(sender: CommandSender, args: Array<String>): Boolean
}
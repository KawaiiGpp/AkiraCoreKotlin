package com.akira.core.api.command

/**
 * 指令元素
 * - 指令路径 [CommandRoute] 的最小构成元素
 *
 * 字面值与动态值：
 * - 字面值：内容固定，用于区分不同指令路径分支
 * - 动态值：占位符，用于捕获用户的输入
 *
 * @property name 元素名，即内容
 * @property displayName 格式化以对外展示的字符串
 * @property isLiteral 指示该元素是否为字面值
 * @property isVariable 指示该元素是否为动态值
 *
 * @throws IllegalArgumentException 当 [name] 为空白值，或包含空格时抛出
 */
class CommandElement(val name: String, val isLiteral: Boolean = true) {
    val displayName = if (isLiteral) name else "<$name>"
    val isVariable = !isLiteral

    init {
        require(!name.contains(Regex("\\s"))) { "Command element's name should have no space: $name." }
        require(name.isNotBlank()) { "Command element's name cannot be blank." }
    }

    /**
     * 指示该元素是否会与其他元素冲突，导致无法区分。
     */
    fun conflictsWith(that: CommandElement): Boolean {
        return when {
            // 一个是字面值，另一个是动态值，不构成冲突；
            isLiteral != that.isLiteral -> false
            // 如果两个都是动态值，无从区分，必然冲突；
            isVariable -> true
            // 如果两个都是字面值，内容一致，必然冲突。
            else -> name == that.name
        }
    }

    companion object {
        /**
         * 从字符串解析指令元素。
         * - 开头为 `#` 将自动识别为动态值，否则识别为字面值
         *
         * 示例：
         * - `#name` 识别为 `<name>`
         * - `set` 识别为 `set`
         */
        fun parse(expression: String): CommandElement {
            val isVariable = expression.startsWith('#')

            return if (!isVariable) CommandElement(expression, true)
            else CommandElement(expression.substring(1), false)
        }
    }
}
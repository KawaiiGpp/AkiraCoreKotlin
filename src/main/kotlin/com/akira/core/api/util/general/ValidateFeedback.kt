package com.akira.core.api.util.general

class ValidateFeedback {
    private val list = mutableListOf<String>()
    val content get() = list.toList()

    val passed get() = list.isEmpty()
    val failed get() = list.isNotEmpty()

    fun add(line: String) {
        list.add(line)
    }

    fun add(lines: Collection<String>) {
        list.addAll(lines)
    }
}
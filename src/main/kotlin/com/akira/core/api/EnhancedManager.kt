package com.akira.core.api

/**
 * 增强注册管理器
 *
 * - 基于 [Manager]，把键统一为字符串类型，旨在简化注册
 * - 注册时，键由 [transform] 动态生成
 *
 * @param E 被管理元素的类型
 */
abstract class EnhancedManager<E> : Manager<String, E>() {
    /**
     * 注册一个元素，键根据 [element] 动态生成。
     *
     * @see Manager.register
     */
    open fun register(element: E) = transform(element).also { super.register(it, element) }

    /**
     * 定义通过 [element] 生成注册键的方式。
     *
     * 注意：需避免不同对象生成同样的键而导致异常。
     */
    protected abstract fun transform(element: E): String
}
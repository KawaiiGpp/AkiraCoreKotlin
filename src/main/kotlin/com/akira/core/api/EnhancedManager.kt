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
     * 注册一个元素，键根据元素动态生成。
     *
     * 该方法先调用 [transform] 传入 [element] 生成注册键，
     * 再调用 [Manager.register] 注册元素。
     *
     * @param element 元素
     * @see Manager.register
     */
    open fun register(element: E) = transform(element).also { super.register(it, element) }

    /**
     * 定义如何通过被注册元素生成注册键。
     *
     * 注意：需避免不同对象生成同样的键而导致异常。
     *
     * @param element 被注册元素
     * @return 由被注册元素动态生成的键
     */
    protected abstract fun transform(element: E): String
}
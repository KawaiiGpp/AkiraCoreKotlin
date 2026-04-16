package com.akira.core.api

/**
 * 增强注册管理器
 *
 * 基于 [Manager] 类，把键统一为字符串类型，旨在简化注册。
 *
 * 注册时，键的内容由元素动态生成，
 * 需实现 [transform] 以定义生成行为。
 *
 * @param E 被管理元素的类型
 */
abstract class EnhancedManager<E> : Manager<String, E>() {
    /**
     * 注册一个元素，键根据元素动态生成。
     *
     * 该方法先调用 [transform] 通过 [element] 生成注册键，
     * 再调用 [Manager.register] 注册元素。
     *
     * @param element 元素
     * @see Manager.register
     */
    open fun register(element: E) = transform(element).also { super.register(it, element) }

    /**
     * 定义如何通过被注册元素生成注册键。
     *
     * 注意：尽可能确保不同的对象必然生成不同的键，
     * 否则可能因键被重复注册导致异常。
     *
     * @param element 被注册元素
     * @return 由被注册元素动态生成的键
     */
    protected abstract fun transform(element: E): String
}
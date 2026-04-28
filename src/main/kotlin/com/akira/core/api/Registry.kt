package com.akira.core.api

import com.akira.core.api.util.general.noSuchElm
import java.util.*

/**
 * 注册管理器
 *
 * - 内部维护一份映射表，以统一注册、卸载和获取
 * - 基于 [HashMap]，需注意线程问题
 *
 * @param K 注册键的类型
 * @param E 被管理元素的类型
 */
abstract class Registry<K, E> {
    /**
     * 内部映射表
     */
    protected val registry: MutableMap<K, E> = HashMap()

    /**
     * 内部映射表的只读视图
     */
    val registryView: Map<K, E> get() = Collections.unmodifiableMap(registry)

    /**
     * 注册一个元素。
     *
     * @throws IllegalArgumentException 当 [key] 已被注册时抛出
     */
    open fun register(key: K, element: E) {
        require(!this.isRegistered(key)) { "Key $key already registered." }
        registry.put(key, element)
    }

    /**
     * 卸载一个元素。
     *
     * @throws IllegalArgumentException 当 [key] 未被注册时抛出
     */
    open fun unregister(key: K) {
        require(this.isRegistered(key)) { "Key $key not registered." }
        registry.remove(key)
    }

    /**
     * 清空映射表。
     */
    open fun clear() = registry.clear()

    /**
     * 判断 [key] 是否已被注册。
     */
    fun isRegistered(key: K): Boolean = registry.containsKey(key)

    /**
     * 通过 [key] 获取元素。
     * - 若该元素不存在，则返回 `null`
     */
    fun get(key: K): E? = registry[key]

    /**
     * 通过 [key] 获取元素。
     * - 若该元素不存在，则返回 [default] 的求值
     */
    fun getOrElse(key: K, default: () -> E): E = registry[key] ?: default()

    /**
     * 通过 [key] 获取元素。
     *
     * @throws NoSuchElementException 当 [key] 不存在对应元素时抛出
     */
    fun getOrThrow(key: K): E = registry[key] ?: noSuchElm("No element present for key: $key")
}
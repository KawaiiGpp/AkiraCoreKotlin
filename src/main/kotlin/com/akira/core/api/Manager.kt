package com.akira.core.api

import com.akira.core.api.util.general.noSuchElm
import java.util.*

/**
 * 注册管理器
 *
 * 内部维护一份映射表，用于统一对元素进行注册、卸载和获取。
 * 基于 [HashMap]，需注意线程问题。
 *
 * - 使用：继承本类，定义键值类型以便明确用途
 * - 进阶使用：可通过覆写扩展 [register]、[unregister]、[clear] 的功能
 *
 * @param K 注册键的类型
 * @param E 被管理元素的类型
 */
abstract class Manager<K, E> {
    protected val registry: MutableMap<K, E> = HashMap()

    /**
     * 内部映射表的只读视图
     */
    val registryView: Map<K, E> get() = Collections.unmodifiableMap(registry)

    /**
     * 注册一个元素。
     *
     * @throws IllegalArgumentException 当该键已被注册时抛出
     */
    open fun register(key: K, element: E) {
        require(!this.isRegistered(key)) { "Key $key already registered." }
        registry.put(key, element)
    }

    /**
     * 卸载一个元素。
     *
     * @throws IllegalArgumentException 当该键未被注册时抛出
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
     * 判断该键是否已被注册。
     */
    fun isRegistered(key: K): Boolean = registry.containsKey(key)

    /**
     * 获取与 [key] 对应的元素，不存在则返回 `null`。
     */
    fun get(key: K): E? = registry[key]

    /**
     * 获取与 [key] 对应的元素，不存在则返回 [default] 的结果。
     */
    fun getOrElse(key: K, default: () -> E): E = registry[key] ?: default()

    /**
     * 获取与 [key] 对应的元素，若不存在则抛出异常。
     *
     * @throws NoSuchElementException 当键不存在时抛出
     */
    fun getOrThrow(key: K): E = registry[key] ?: noSuchElm("No element present for key: $key")
}
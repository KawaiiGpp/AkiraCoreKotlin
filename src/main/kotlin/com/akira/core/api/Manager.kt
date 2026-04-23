package com.akira.core.api

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
    protected val map: MutableMap<K, E> = HashMap()

    /**
     * 内部映射表的只读视图
     */
    val container: Map<K, E> get() = Collections.unmodifiableMap(map)

    /**
     * 注册一个元素。
     *
     * @param key 键
     * @param element 元素
     * @throws IllegalArgumentException 当该键已被注册时抛出
     */
    open fun register(key: K, element: E) {
        require(!this.isRegistered(key)) { "Key $key already registered." }
        map.put(key, element)
    }

    /**
     * 卸载一个元素。
     *
     * @param key 键
     * @throws IllegalArgumentException 当该键未被注册时抛出
     */
    open fun unregister(key: K) {
        require(this.isRegistered(key)) { "Key $key not registered." }
        map.remove(key)
    }

    /**
     * 清空映射表。
     */
    open fun clear() = map.clear()

    /**
     * 判断该键是否已被注册。
     *
     * @param key 键
     * @return 若已注册返回 `true`，否则返回 `false`
     */
    fun isRegistered(key: K): Boolean = map.containsKey(key)

    /**
     * 根据键获取对应的元素。
     *
     * @param key 键
     * @return 若键存在则返回对应元素，否则返回 `null`
     */
    fun get(key: K): E? = map[key]

    /**
     * 根据键获取对应的元素，若键不存在则获取默认值并返回。
     *
     * @param key 键
     * @param default 若键不存在，则执行该代码块以获取默认值
     * @return 若键存在则返回对应元素，否则返回默认值
     */
    fun getOrElse(key: K, default: () -> E): E = map[key] ?: default()

    /**
     * 根据键获取对应的元素，若键不存在则抛出异常。
     *
     * @param key 键
     * @return 若键存在则返回对应元素，否则抛出异常
     * @throws NoSuchElementException 当键不存在时抛出
     */
    fun getOrThrow(key: K): E {
        return map[key] ?: throw NoSuchElementException("No element present for key: $key")
    }
}
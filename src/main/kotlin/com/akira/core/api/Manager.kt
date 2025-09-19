package com.akira.core.api

abstract class Manager<K, E> {
    protected val map: MutableMap<K, E> = HashMap()
    val container get() = map.toMap()

    fun register(key: K, element: E) {
        require(!this.isRegistered(key)) { "Key $key already registered." }
        map.put(key, element)
    }

    fun unregister(key: K) {
        require(this.isRegistered(key)) { "Key $key not registered." }
        map.remove(key)
    }

    fun isRegistered(key: K): Boolean = map.containsKey(key)

    fun get(key: K): E? = map[key]

    fun get(key: K, default: () -> E): E = map[key] ?: default()

    fun get(key: K, default: E): E = map[key] ?: default

    fun getNonNull(key: K): E = requireNotNull(map[key]) { "Nothing found for Key: $key" }

    fun clear() = map.clear()
}
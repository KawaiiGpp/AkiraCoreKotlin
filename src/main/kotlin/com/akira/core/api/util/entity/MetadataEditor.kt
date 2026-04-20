package com.akira.core.api.util.entity

import com.akira.core.api.AkiraPlugin
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue
import org.bukkit.metadata.Metadatable

/**
 * 实体元数据编辑器
 *
 * 封装了对实体 [MetadataValue] 的操作：
 * - 查询实体的指定元数据是否存在
 * - 获取实体的元数据
 * - 设置或删除实体的元数据
 *
 * 根据 `Metadata API` 的特性，
 * 每个插件拥有其独立的元数据的储存空间，
 * 无需担心插件之间同路径元数据的冲突。
 *
 * @property plugin 所属插件
 * @property owner 编辑对象
 */
class MetadataEditor(private val plugin: AkiraPlugin, private val owner: Metadatable) {
    /**
     * 删除实体身上指定的元数据。
     *
     * @param path 路径
     */
    fun remove(path: String) = owner.removeMetadata(path, plugin)

    /**
     * 为实体设置元数据。
     *
     * 若该路径下已有元数据，将被静默覆盖。
     *
     * @param path 路径
     * @param any 数据
     */
    fun set(path: String, any: Any) = owner.setMetadata(path, FixedMetadataValue(plugin, any))

    /**
     * 从实体身上获取元数据。
     *
     * @param path 路径
     * @return 元数据
     */
    fun get(path: String): MetadataValue? = owner.getMetadata(path).firstOrNull { plugin == it.owningPlugin }

    /**
     * 指示实体是否在指定路径上设有元数据。
     *
     * @param path 路径
     * @return 若路径存在元数据则返回 `true`，否则返回 `false`
     */
    fun has(path: String): Boolean = owner.getMetadata(path).any { plugin == it.owningPlugin }
}
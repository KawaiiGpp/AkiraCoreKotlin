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
 * 每个插件有独立储存空间，不存在插件间路径冲突。
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
     * @param any 元数据值
     */
    fun set(path: String, any: Any) = owner.setMetadata(path, FixedMetadataValue(plugin, any))

    /**
     * 从实体身上获取元数据。
     *
     * @param path 路径
     * @return 指定元数据
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
package com.akira.core.api.util.entity

import com.akira.core.api.AkiraPlugin
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue
import org.bukkit.metadata.Metadatable

/**
 * 实体元数据编辑器
 *
 * - 封装了有关 [MetadataValue] 的操作
 * - 每个插件独立储存，可避免插件之间的路径冲突
 *
 * @param plugin 所属插件
 * @param owner 编辑对象
 */
class MetadataEditor(private val plugin: AkiraPlugin, private val owner: Metadatable) {
    /**
     * 删除 [MetadataValue]，若不存在则不作处理。
     */
    fun remove(path: String) = owner.removeMetadata(path, plugin)

    /**
     * 为 [path] 设置 [MetadataValue]，若已存在则覆盖。
     */
    fun set(path: String, any: Any) = owner.setMetadata(path, FixedMetadataValue(plugin, any))

    /**
     * 获取 [MetadataValue]，若不存在则返回 `null`。
     */
    fun get(path: String): MetadataValue? = owner.getMetadata(path).firstOrNull { plugin == it.owningPlugin }

    /**
     * 判断 [path] 下是否存在 [MetadataValue]。
     */
    fun has(path: String): Boolean = owner.getMetadata(path).any { plugin == it.owningPlugin }
}
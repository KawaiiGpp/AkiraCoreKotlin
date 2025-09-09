package com.akira.core.api.util.entity

import com.akira.core.api.AkiraPlugin
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue
import org.bukkit.metadata.Metadatable

class MetadataEditor(val plugin: AkiraPlugin, val owner: Metadatable) {
    fun remove(path: String) = owner.removeMetadata(path, plugin)

    fun set(path: String, o: Any) = owner.setMetadata(path, FixedMetadataValue(plugin, o))

    fun get(path: String): MetadataValue? = owner.getMetadata(path).firstOrNull { plugin == it.owningPlugin }

    fun has(path: String): Boolean = owner.getMetadata(path).any { plugin == it.owningPlugin }
}
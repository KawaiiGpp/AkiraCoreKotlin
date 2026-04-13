package com.akira.core.api.util.item

import com.akira.core.api.AkiraPlugin
import com.akira.core.api.util.general.specifyUniqueId
import com.akira.core.api.util.world.specifyAttributeModifier
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemAttributeEditor(
    private val meta: ItemMeta,
    private val attribute: Attribute,
    private val namespace: String?
) {
    private val modifiers: Collection<AttributeModifier>
        get() {
            val errorMsg = "Attribute modifiers not supported by this item meta."
            val map = meta.attributeModifiers ?: throw NullPointerException(errorMsg)

            return map[attribute]
        }

    fun remove(name: String) {
        val uniqueId = specifyUniqueId(name, namespace)

        modifiers.filter { it.uniqueId == uniqueId }
            .forEach { meta.removeAttributeModifier(attribute, it) }
    }

    fun set(name: String, value: Double, operation: Operation) {
        val modifier = specifyAttributeModifier(name, value, operation, namespace)

        this.remove(name)
        meta.addAttributeModifier(attribute, modifier)
    }

    fun apply(item: ItemStack) = meta.let { item.itemMeta = it }

    companion object {
        fun forItemMeta(item: ItemStack, attribute: Attribute, plugin: AkiraPlugin): ItemAttributeEditor {
            val meta = requireValidMeta(item)
            val map = meta.attributeModifiers

            requireNotNull(map) { "This item (type=${item.type}) doesn't support attribute modifiers." }
            return ItemAttributeEditor(meta, attribute, plugin.name)
        }
    }
}
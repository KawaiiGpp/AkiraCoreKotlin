package com.akira.core.api.util.entity

import com.akira.core.api.util.general.specifyUniqueId
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.entity.LivingEntity

class AttributeEditor(
    private val instance: AttributeInstance,
    private val namespace: String? = null
) {
    var base
        get() = instance.baseValue
        set(value) = value.let { instance.baseValue = it }

    fun add(name: String, value: Double, operation: Operation = Operation.ADD_NUMBER) {
        this.removeIfExisting(name)
        instance.addModifier(AttributeModifier(specifyUniqueId(name, namespace), name, value, operation))
    }

    fun remove(name: String) =
        instance.removeModifier(requireNotNull(this.get(name)) { "Modifier $name doesn't exist." })

    fun removeIfExisting(name: String) {
        this.get(name)?.let { instance.removeModifier(it) }
    }

    fun get(name: String): AttributeModifier? =
        instance.modifiers.firstOrNull { it.uniqueId == specifyUniqueId(name, namespace) }

    fun has(name: String): Boolean =
        instance.modifiers.any { it.uniqueId == specifyUniqueId(name, namespace) }

    companion object {
        fun forEntity(target: LivingEntity, attribute: Attribute, namespace: String): AttributeEditor =
            AttributeEditor(target.getNonNullAttribute(attribute), namespace)
    }
}

package com.akira.core.api.util.entity

import com.akira.core.api.AkiraPlugin
import com.akira.core.api.util.general.specifyUniqueId
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.entity.LivingEntity
import java.util.*

/**
 * 实体属性编辑器
 *
 * - 封装对实体 [AttributeInstance] 的操作
 * - [AttributeModifier] 的 [UUID] 基于名称与 [namespace] 生成
 *
 * @property instance 属性实例
 * @property namespace 命名空间
 */
class AttributeEditor(
    private val instance: AttributeInstance,
    private val namespace: String? = null
) {
    /**
     * 创建一个针对 [LivingEntity] 的编辑器实例。
     *
     * - 通过 [LivingEntity.requireAttribute] 获取属性实例
     * - 自动以 [AkiraPlugin.name] 为命名空间
     *
     * @throws IllegalArgumentException 当实体不支持该属性类型
     */
    constructor(entity: LivingEntity, attribute: Attribute, plugin: AkiraPlugin) :
            this(entity.requireAttribute(attribute), plugin.name)

    var base
        get() = instance.baseValue
        set(value) = value.let { instance.baseValue = it }

    val value get() = instance.value

    val defaultValue get() = instance.defaultValue

    /**
     * 添加新的 [AttributeModifier]，若已存在则覆盖。
     */
    fun add(
        name: String,
        value: Double = 0.0,
        operation: Operation = Operation.ADD_NUMBER
    ) {
        this.remove(name)

        val uniqueId = this.generateUniqueId(name)
        val modifier = AttributeModifier(uniqueId, name, value, operation)
        instance.addModifier(modifier)
    }

    /**
     * 移除 [AttributeModifier]，若不存在则不作处理。
     */
    fun remove(name: String) {
        instance.removeModifier(generateUniqueId(name))
    }

    /**
     * 获取 [AttributeModifier]，若不存在则返回 `null`。
     */
    fun get(name: String): AttributeModifier? {
        return instance.getModifier(generateUniqueId(name))
    }

    /**
     * 判断名为 [name] 的 [AttributeModifier] 是否存在。
     */
    fun contains(name: String): Boolean {
        return instance.modifiers.any { it.uniqueId == generateUniqueId(name) }
    }

    private fun generateUniqueId(name: String) = specifyUniqueId(name, namespace)
}

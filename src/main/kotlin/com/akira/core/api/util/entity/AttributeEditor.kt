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
 * - 封装了对于实体 [AttributeInstance] 的操作
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
     * 创建一个针对实体的属性编辑器
     *
     * - 以 [LivingEntity] 为对象
     * - 以 [AkiraPlugin.name] 为命名空间
     *
     * @param entity 编辑对象
     * @param attribute 编辑属性类型
     * @param plugin 所属插件
     * @throws IllegalArgumentException 当实体不支持该属性类型
     */
    constructor(entity: LivingEntity, attribute: Attribute, plugin: AkiraPlugin) :
            this(entity.requireAttribute(attribute), plugin.name)

    /**
     * 属性实例的基础值
     * @see [AttributeInstance.baseValue]
     */
    var base
        get() = instance.baseValue
        set(value) = value.let { instance.baseValue = it }

    /**
     * 属性实例的计算终值
     * @see [AttributeInstance.value]
     */
    val value get() = instance.value

    /**
     * 属性的默认值
     * @see [AttributeInstance.defaultValue]
     */
    val defaultValue get() = instance.defaultValue

    /**
     * 添加修饰符到实例，若已存在则覆盖。
     *
     * @param name 修饰符名称
     * @param value 修饰值
     * @param operation 修饰行为
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
     * 从属性实例移除指定修饰符。该方法是幂等的。
     *
     * @param name 修饰符名称
     */
    fun remove(name: String) = instance.removeModifier(generateUniqueId(name))

    /**
     * 从属性实例获取指定的修饰符。
     *
     * @param name 修饰符名称
     * @return 修饰符实例，不存在则返回 `null`
     */
    fun get(name: String): AttributeModifier? = instance.getModifier(generateUniqueId(name))

    /**
     * 指示该属性实例是否存在该修饰符。
     *
     * @param name 修饰符名称
     * @return 若存在则返回 `true`，否则返回 `false`
     */
    fun has(name: String): Boolean = instance.modifiers.any { it.uniqueId == generateUniqueId(name) }

    private fun generateUniqueId(name: String) = specifyUniqueId(name, namespace)
}

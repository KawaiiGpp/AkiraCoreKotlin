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
 * 封装了对于实体 [AttributeInstance] 的操作：
 * - 获取或修改基础值
 * - 增加或删减 [AttributeModifier]
 *
 * 为实体增减 [AttributeModifier] 时，
 * 其唯一标识符 [UUID] 根据修饰符名与 [namespace] 动态生成。
 *
 * @property instance 来自实体的属性实例
 * @property namespace 属性修饰符的命名空间
 */
class AttributeEditor(
    private val instance: AttributeInstance,
    private val namespace: String? = null
) {
    /**
     * 创建一个实体属性编辑器，以实体为对象，以插件名为命名空间。
     *
     * @param entity 需要编辑的实体
     * @param attribute 需要编辑的属性类型
     * @param plugin 所属插件
     * @throws IllegalArgumentException 当实体不支持该属性类型
     */
    constructor(entity: LivingEntity, attribute: Attribute, plugin: AkiraPlugin) :
            this(entity.requireAttribute(attribute), plugin.name)

    /**
     * 代理 [AttributeInstance.baseValue]
     */
    var base
        get() = instance.baseValue
        set(value) = value.let { instance.baseValue = it }

    /**
     * 代理 [AttributeInstance.value]
     */
    val value get() = instance.value

    /**
     * 代理 [AttributeInstance.defaultValue]
     */
    val defaultValue get() = instance.defaultValue

    /**
     * 添加修饰符到属性实例中，若已存在则直接覆盖。
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
     * 从属性实例中删去指定修饰符。
     *
     * 该操作是幂等的，
     * 若修饰符不存在将不进行任何操作。
     *
     * @param name 修饰符名称
     */
    fun remove(name: String) = instance.removeModifier(generateUniqueId(name))

    /**
     * 从属性实例中获取指定的修饰符实例。
     *
     * @param name 修饰符名称
     * @return 修饰符实例，不存在则返回 `null`
     */
    fun get(name: String): AttributeModifier? = instance.getModifier(generateUniqueId(name))

    /**
     * 指示该属性实例中是否存在指定的修饰符。
     *
     * @param name 修饰符名称
     * @return 若存在则返回 `true`，否则返回 `false`
     */
    fun has(name: String): Boolean = instance.modifiers.any { it.uniqueId == generateUniqueId(name) }

    private fun generateUniqueId(name: String) = specifyUniqueId(name, namespace)
}

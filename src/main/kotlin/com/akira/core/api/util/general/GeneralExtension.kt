package com.akira.core.api.util.general

import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import kotlin.math.min

/**
 * 无效化 [EntityDamageEvent] 中的减伤修饰符。
 *
 * - 当所有减伤算法失效，扣血行为类似于真伤，默认不绕过伤害吸收
 *
 * @param bypassAbsorption 是否绕过伤害吸收，默认为 `false`
 */
@Suppress("DEPRECATION")
fun EntityDamageEvent.bypassVanillaModifiers(bypassAbsorption: Boolean = false) {
    val entity = this.entity as? LivingEntity
    val modifiers = DamageModifier.entries
        .asSequence()
        .filter { it != DamageModifier.BASE }
        .filter { this.isApplicable(it) }

    for (modifier in modifiers) {
        if (modifier != DamageModifier.ABSORPTION || bypassAbsorption) this.setDamage(modifier, 0.0)
        else entity?.let { setDamage(modifier, -min(it.absorptionAmount, damage)) }
    }
}

/**
 * 通过 [predicate] 断言合法性。
 *
 * - 检验通过：返回原值
 * - 检验不通过：抛出异常，信息由 [message] 动态生成
 *
 * @throws IllegalArgumentException 当校验不通过时抛出
 */
inline fun <T> T.ensure(
    predicate: (T) -> Boolean,
    message: (T) -> String = { "Value not legit: $it" }
): T {
    require(predicate(this)) { message(this) }
    return this
}
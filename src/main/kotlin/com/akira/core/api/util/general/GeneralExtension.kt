package com.akira.core.api.util.general

import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import kotlin.math.min

/**
 * 无效化事件中的减伤修饰符。
 *
 * 当所有减伤算法失效，扣血行为类似于真伤逻辑，默认不绕过伤害吸收。
 *
 * @param bypassAbsorption 是否使伤害吸收失效，默认为 `false`
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
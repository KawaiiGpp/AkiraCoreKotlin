package com.akira.core.api.util.item

import com.akira.core.api.util.text.toComponent
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class ItemBuilder {
    var material: Material? = null
    var displayName: String? = null
    var lore: MutableList<String> = mutableListOf()
    var amount: Int = 1
    var unbreakable: Boolean = false
    var unsafeAmount: Boolean = false
    var flags: MutableList<ItemFlag> = mutableListOf()
    var enchantments: MutableList<Pair<Enchantment, Int>> = mutableListOf()

    fun addLores(vararg lines: String) = lines.forEach(this.lore::add)

    fun addFlags(vararg flags: ItemFlag) = flags.forEach(this.flags::add)

    fun addEnchant(ench: Enchantment, level: Int) = enchantments.add(ench to level)

    fun build(): ItemStack {
        requireNotNull(material) { "Material cannot be null." }

        val material = this.material!!
        require(material.isItem) { "Material must be an Item." }
        require(amount > 0) { "Item amount must be > 0." }
        require(unsafeAmount || amount <= material.maxStackSize) { "Item amount limit exceeded." }

        val item = ItemStack(material, amount)
        val meta = item.itemMeta
        requireNotNull(meta) { "Item meta from ${material.name} is null." }

        displayName?.let { meta.displayName(it.toComponent()) }
        if (!lore.isEmpty()) meta.lore(lore.map(String::toComponent))
        meta.isUnbreakable = unbreakable
        flags.forEach(meta.itemFlags::add)
        enchantments.forEach { (ench, lvl) -> meta.addEnchant(ench, lvl, true) }

        item.itemMeta = meta
        return item
    }
}
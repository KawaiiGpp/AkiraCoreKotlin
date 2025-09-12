package com.akira.core.api.util.item

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

class DyedArmorGenerator(private val color: Color) {
    fun createHelmet(): ItemStack = create(Material.LEATHER_HELMET)

    fun createChestplate(): ItemStack = create(Material.LEATHER_CHESTPLATE)

    fun createLeggings(): ItemStack = create(Material.LEATHER_LEGGINGS)

    fun createBoots(): ItemStack = create(Material.LEATHER_BOOTS)

    private fun create(material: Material): ItemStack {
        val item = ItemStack(material)
        val meta = requireValidMeta(item)

        require(meta is LeatherArmorMeta) {
            "Only items with leather armor meta can be create."
        }

        meta.setColor(color)
        item.itemMeta = meta
        return item
    }
}
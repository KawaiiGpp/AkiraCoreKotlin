package com.akira.core.api.util.item

import com.akira.core.api.util.general.illegalArg
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

/**
 * 皮革盔甲染色工厂
 *
 * @param color 皮革颜色
 */
class DyedArmorFactory(private val color: Color) {
    fun createHelmet(): ItemStack = create(Material.LEATHER_HELMET)

    fun createChestplate(): ItemStack = create(Material.LEATHER_CHESTPLATE)

    fun createLeggings(): ItemStack = create(Material.LEATHER_LEGGINGS)

    fun createBoots(): ItemStack = create(Material.LEATHER_BOOTS)

    /**
     * 封装物品创建以及染色的逻辑。
     *
     * @throws IllegalArgumentException 当传入的 [material] 不支持染色时抛出
     */
    private fun create(material: Material): ItemStack {
        val item = ItemStack(material)
        val success = item.editMeta(LeatherArmorMeta::class.java) { it.setColor(color) }

        if (success) return item
        illegalArg("Cannot apply leather color to $material.")
    }
}
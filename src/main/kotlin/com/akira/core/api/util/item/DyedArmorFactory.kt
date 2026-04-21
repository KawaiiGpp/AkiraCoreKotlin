package com.akira.core.api.util.item

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

/**
 * 皮革盔甲染色工厂
 *
 * 该工具用于创建指定颜色的皮革盔甲。
 *
 * @property color 皮革颜色
 */
class DyedArmorFactory(private val color: Color) {
    fun createHelmet(): ItemStack = create(Material.LEATHER_HELMET)

    fun createChestplate(): ItemStack = create(Material.LEATHER_CHESTPLATE)

    fun createLeggings(): ItemStack = create(Material.LEATHER_LEGGINGS)

    fun createBoots(): ItemStack = create(Material.LEATHER_BOOTS)

    /**
     * 封装了物品创建以及染色的逻辑。
     *
     * @param material 物品类型，须兼容染色
     * @return 已染色物品
     * @throws IllegalArgumentException 当传入 [material] 不支持染色
     */
    private fun create(material: Material): ItemStack {
        val item = ItemStack(material)
        val success = item.editMeta(LeatherArmorMeta::class.java) { it.setColor(color) }

        if (success) return item
        throw IllegalArgumentException("Cannot apply leather color to $material.")
    }
}
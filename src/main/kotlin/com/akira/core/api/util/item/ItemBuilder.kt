package com.akira.core.api.util.item

import com.akira.core.api.util.item.ItemBuilder.Companion.build
import com.akira.core.api.util.text.toComponent
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

/**
 * 物品构建器
 *
 * 简化创建 [ItemStack] 的复杂逻辑，
 * 调用 [build] 即可基于已配置参数新建 [ItemStack]。
 *
 * @property material 物品类型，默认 `null`，必填
 * @property amount 数量，默认 `1`
 * @property displayName 显示名称，默认 `null`
 * @property lore 物品描述，默认为空列表
 * @property unbreakable 是否无法破坏，默认 `false`
 * @property unsafeAmount 无数量限制，默认 `false`
 * @property flags 属性隐藏，默认为空列表
 * @property enchantments 附魔，默认为空列表
 */
class ItemBuilder {
    var material: Material? = null
    var amount: Int = 1
    var displayName: String? = null
    var lore: MutableList<String> = mutableListOf()

    var unbreakable: Boolean = false
    var unsafeAmount: Boolean = false
    var flags: MutableSet<ItemFlag> = mutableSetOf()
    var enchantments: MutableMap<Enchantment, Int> = mutableMapOf()

    /**
     * 添加单行物品描述。
     */
    fun addLore(line: String) {
        lore.add(line)
    }

    /**
     * 添加物品属性隐藏标签，若重复将覆盖。
     */
    fun addFlag(flag: ItemFlag) {
        flags.add(flag)
    }

    /**
     * 批量添加物品描述。
     */
    fun addLores(vararg lines: String) = lines.forEach(this.lore::add)

    /**
     * 批量添加物品属性隐藏标签，若重复将覆盖。
     */
    fun addFlags(vararg flags: ItemFlag) = flags.forEach(this.flags::add)

    /**
     * 添加附魔信息。若已有该类型，则新等级覆盖旧等级。
     */
    fun addEnchant(ench: Enchantment, level: Int) {
        enchantments[ench] = level
    }

    /**
     * 根据已设定的参数构建 [ItemStack]。
     *
     * @throws IllegalArgumentException 当参数校验不通过时由 [initResult] 抛出
     * @see initResult
     */
    fun build(): ItemStack {
        val item = this.initResult()

        item.editMeta { meta ->
            displayName?.let { meta.displayName(it.toComponent()) }
            if (lore.isNotEmpty()) meta.lore(lore.map(String::toComponent))

            flags.forEach(meta.itemFlags::add)
            enchantments.forEach { (ench, lvl) -> meta.addEnchant(ench, lvl, true) }

            meta.isUnbreakable = unbreakable
        }

        return item
    }

    /**
     * 校验已设定参数并初始化 [ItemStack]。
     *
     * 满足以下任意一条，则抛出异常：
     * - 若 [material] 这一必填项未设定
     * - 若 [material] 的 [Material.isItem] 为 `false`
     * - 若 [amount] 为零或负数
     * - 未启用 [unsafeAmount] 时 [amount] 越界
     * - 若构建的 [ItemStack.itemMeta] 为 `null`
     *
     * @throws IllegalArgumentException 当校验不通过
     */
    private fun initResult(): ItemStack {
        val material = this.material
        requireNotNull(material) { "Material is not set." }
        require(material.isItem) { "Material $material is not an Item." }
        require(amount > 0) { "Item amount must be > 0, but actual: $amount." }

        val limit = material.maxStackSize
        require(unsafeAmount || amount <= limit) { "Item amount limit exceeded: $amount/$limit." }

        val item = ItemStack(material, amount)
        requireNotNull(item.itemMeta) { "Item meta from $material is null. " }

        return item
    }

    companion object {
        /**
         * 使用 `DSL` 语法构建 [ItemStack]。
         */
        fun build(block: ItemBuilder.() -> Unit) = ItemBuilder().apply(block).build()
    }
}
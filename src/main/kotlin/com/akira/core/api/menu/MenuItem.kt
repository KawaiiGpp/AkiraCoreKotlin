package com.akira.core.api.menu

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * 菜单物品声明蓝图
 *
 * - 只声明结构与行为，不持有实例，不储存状态
 *
 * @property item 物品实例
 */
abstract class MenuItem(private val item: ItemStack) {
    /**
     * 复制物品实例。
     */
    fun copyItem() : ItemStack = item.clone()

    /**
     * 为 [player] 创建专属的物品实例。
     */
    open fun create(player: Player): ItemStack = copyItem()

    /**
     * 处理来自 [clicker] 的点击。
     */
    open fun click(clicker: Player) {}
}
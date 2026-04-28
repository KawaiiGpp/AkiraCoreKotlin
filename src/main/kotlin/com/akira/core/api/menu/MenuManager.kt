package com.akira.core.api.menu

import com.akira.core.api.Registry
import org.bukkit.inventory.Inventory

/**
 * 菜单声明蓝图管理器
 *
 * - 储存已注册的菜单蓝图实例
 * - 可通过 [Inventory] 或注册键获取 [Menu] 对象
 */
class MenuManager : Registry<String, Menu>() {
    /**
     * 通过 [inventory] 寻找匹配的菜单蓝图。
     */
    fun find(inventory: Inventory): Menu? {
        return registry.values.firstOrNull { it.matches(inventory) }
    }
}
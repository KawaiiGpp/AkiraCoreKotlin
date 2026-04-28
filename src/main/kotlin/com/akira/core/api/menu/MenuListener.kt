package com.akira.core.api.menu

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * 菜单事件处理器
 *
 * - 自动拦截原版的物品栏操作，如取出或放入物品
 * - 自动筛选并分发点击事件到相关的 [Menu] 实例
 * - 继承该类后，可重写方法以进行更详细的配置
 *
 * @param manager 接收事件分发的菜单管理器
 */
abstract class MenuListener(private val manager: MenuManager) : Listener {
    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        if (!shouldHandleEvent(event)) return
        val clicker = event.whoClicked as? Player ?: return
        val menu = manager.find(event.inventory) ?: return

        event.isCancelled = true
        if (event.inventory != event.clickedInventory) return
        if (!shouldCallMenu(event)) return

        runCatching { menu.click(clicker, event.slot) }
            .onFailure(this::handleException)
    }

    /**
     * 总开关：是否处理本次点击。
     */
    open fun shouldHandleEvent(event: InventoryClickEvent): Boolean = true

    /**
     * 是否把点击事件分发给 [MenuItem]。
     */
    open fun shouldCallMenu(event: InventoryClickEvent): Boolean = true

    /**
     * 事件分发和处理过程发生异常将触发该回调。
     */
    open fun handleException(exception: Throwable) {}
}
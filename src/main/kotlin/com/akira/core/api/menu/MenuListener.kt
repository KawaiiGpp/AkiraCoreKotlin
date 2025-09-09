package com.akira.core.api.menu

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

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

    open fun shouldHandleEvent(event: InventoryClickEvent): Boolean = true

    open fun shouldCallMenu(event: InventoryClickEvent): Boolean = true

    open fun handleException(exception: Throwable) {}
}
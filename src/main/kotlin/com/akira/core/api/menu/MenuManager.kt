package com.akira.core.api.menu

import com.akira.core.api.Manager
import org.bukkit.inventory.Inventory

class MenuManager : Manager<String, Menu>() {
    fun find(inventory: Inventory): Menu? = map.values.firstOrNull { it.matches(inventory) }
}
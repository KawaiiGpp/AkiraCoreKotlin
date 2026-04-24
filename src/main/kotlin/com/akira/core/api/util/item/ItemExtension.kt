package com.akira.core.api.util.item

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * 获取一个通过有效性检查的 [ItemMeta]。
 *
 * @throws IllegalArgumentException 若该 [ItemMeta] 无效。
 */
val ItemStack?.requiredMeta: ItemMeta
    get() {
        requireNotNull(this) { "Item stack is null." }
        require(type.isItem) { "Material $type is not an item." }

        return requireNotNull(itemMeta) {
            "Item stack (type=${type}) has no item meta."
        }
    }
package com.akira.core.api.util.item

import com.akira.core.api.util.general.illegalState
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * 获取一个通过有效性检查的 [ItemMeta]。
 *
 * @throws IllegalStateException 当 [ItemStack.type] 无效或 [ItemStack.meta] 为 `null`
 */
val ItemStack.requiredMeta: ItemMeta
    get() {
        if (!type.isItem) illegalState("Material $type is not an item.")
        if (itemMeta == null) illegalState("Item meta (type=$type) is null.")
        return itemMeta
    }
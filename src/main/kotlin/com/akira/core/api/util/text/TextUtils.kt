package com.akira.core.api.util.text

import org.bukkit.Bukkit

fun debug(content: Any?) = Bukkit.broadcast(content.toString().toComponent())
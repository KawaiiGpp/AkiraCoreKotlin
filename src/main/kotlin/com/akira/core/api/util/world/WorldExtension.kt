package com.akira.core.api.util.world

import org.bukkit.Location

val Location.worldNonNull get() = requireNotNull(world) { "World not defined." }
package com.akira.core.api.util.world

import org.bukkit.Location

val Location.nonNullWorld get() = requireNotNull(world) { "World not defined." }
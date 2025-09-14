package com.akira.core.api.util.player

import com.akira.core.api.AkiraPlugin
import com.akira.core.api.util.general.separateUniqueId
import com.akira.core.api.util.web.fetchHttp
import com.google.gson.JsonParser
import org.bukkit.Bukkit
import java.util.*

fun fetchPlayerUniqueId(
    plugin: AkiraPlugin, name: String,
    exceptionSolver: (Throwable) -> Unit,
    callback: (UUID) -> Unit
) {
    val scheduler = Bukkit.getScheduler()

    fun runSync(runnable: Runnable) =
        scheduler.runTask(plugin, runnable)

    scheduler.runTaskAsynchronously(plugin, Runnable {
        runCatching {
            val response = fetchHttp("https://api.mojang.com/users/profiles/minecraft/$name")
            val json = JsonParser.parseString(response).asJsonObject

            UUID.fromString(separateUniqueId(json.get("id").asString))
        }
            .onSuccess { runSync { callback(it) } }
            .onFailure { runSync { exceptionSolver(it) } }
    })
}
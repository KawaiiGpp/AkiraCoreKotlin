package com.akira.core.api.util.player

import com.akira.core.api.AkiraPlugin
import com.akira.core.api.util.general.launch
import com.akira.core.api.util.general.launchAsync
import com.akira.core.api.util.general.separateUniqueId
import com.akira.core.api.util.web.fetchHttp
import com.google.gson.JsonParser
import java.util.*

/**
 * 通过 `Mojang API` 获取玩家的 [UUID]。
 *
 * - 以 [plugin] 的名义，启动异步拉取数据以避免阻塞
 *
 * @param callback 若成功获取 [UUID]，该回调执行
 * @param exceptionSolver 若有异常，该回调执行
 */
fun fetchPlayerUniqueId(
    plugin: AkiraPlugin,
    playerName: String,
    callback: (UUID) -> Unit,
    exceptionSolver: (Throwable) -> Unit
) {
    plugin.launchAsync {
        val result = runCatching {
            val url = "https://api.mojang.com/users/profiles/minecraft/$playerName"
            val response = fetchHttp(url)
            val json = JsonParser.parseString(response).asJsonObject

            UUID.fromString(separateUniqueId(json.get("id").asString))
        }

        result.onSuccess { plugin.launch { callback(it) } }
        result.onFailure { plugin.launch { exceptionSolver(it) } }
    }
}
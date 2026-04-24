package com.akira.core.api.util.web

import com.akira.core.api.util.general.ioFailure
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.nio.charset.Charset

/**
 * 发起同步 `HTTP GET` 请求。
 *
 * - 该方法为阻塞式调用
 * - 异常全部由调用方处理
 *
 * @param timeout 连接与读取的超时时间
 * @throws IOException 超时、响应码错误等情况时
 */
fun fetchHttp(
    url: String,
    timeout: Int = 10_000,
    encoding: Charset = Charsets.UTF_8
): String {
    fun read(stream: InputStream): String {
        val reader = InputStreamReader(stream, encoding)
        return BufferedReader(reader).use { it.readText() }
    }

    var connection: HttpURLConnection? = null

    try {
        val url = URI(url).toURL()
        connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.connectTimeout = timeout
        connection.readTimeout = timeout

        val code = connection.responseCode

        if (code != 200) {
            val error = connection.errorStream?.let { read(it) }

            if (error == null) ioFailure("Incorrect response code $code from $url.")
            else ioFailure("Incorrect response code $code from $url, with error:\n$error")
        }

        return read(connection.inputStream)
    } finally {
        connection?.disconnect()
    }
}
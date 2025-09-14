package com.akira.core.api.util.web

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

fun fetchHttp(
    url: String, timeout: Int = 10_000,
    encoding: Charset = Charsets.UTF_8
): String {
    var connection: HttpURLConnection? = null

    fun read(stream: InputStream) =
        BufferedReader(InputStreamReader(stream, encoding)).readText()

    try {
        connection = URL(url).openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.connectTimeout = timeout
        connection.readTimeout = timeout

        val code = connection.responseCode

        val suffix = connection.errorStream?.let { " with details: ${read(it)}" } ?: ""
        require(code == 200) { "Incorrect response code $code from $url$suffix" }

        return read(connection.inputStream)
    } finally {
        connection?.disconnect()
    }
}
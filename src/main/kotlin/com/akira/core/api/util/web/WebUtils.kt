package com.akira.core.api.util.web

import java.io.BufferedReader
import java.io.IOException
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

        if (code != 200) {
            val suffix = connection.errorStream?.let { "\n${read(it)}" } ?: ""
            throw IOException("Incorrect response code $code from $url$suffix")
        }

        return read(connection.inputStream)
    } finally {
        connection?.disconnect()
    }
}
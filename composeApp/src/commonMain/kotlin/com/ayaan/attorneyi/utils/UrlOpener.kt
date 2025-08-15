package com.ayaan.attorneyi.utils

import androidx.compose.ui.platform.UriHandler

interface UrlOpener {
    fun openUrl(url: String)
}

class ComposeUrlOpener(private val uriHandler: UriHandler) : UrlOpener {
    override fun openUrl(url: String) {
        try {
            uriHandler.openUri(url)
        } catch (e: Exception) {
            println("Failed to open URL: $url, Error: ${e.message}")
        }
    }
}

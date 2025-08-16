package com.ayaan.attorneyi.utils

interface ShareService {
    fun share(title: String, url: String)
}

expect class ShareServiceProvider() {
    fun getShareService(): ShareService
}
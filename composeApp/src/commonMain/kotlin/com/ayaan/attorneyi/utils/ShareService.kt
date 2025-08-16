package com.ayaan.attorneyi.utils

interface ShareService {
    fun share(title: String, url: String)
    fun shareWithImage(title: String, url: String, imagePath: String)
}

expect class ShareServiceProvider() {
    fun getShareService(): ShareService
}
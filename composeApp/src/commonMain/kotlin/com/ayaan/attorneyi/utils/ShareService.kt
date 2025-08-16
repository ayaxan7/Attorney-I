package com.ayaan.attorneyi.utils

interface ShareService {
    fun share(title: String, url: String)
    fun shareWithImage(title: String, url: String, imagePath: String)
    fun shareWithImageUrl(title: String, url: String, imageUrl: String, callback: (Boolean) -> Unit = {})
}

expect class ShareServiceProvider() {
    fun getShareService(): ShareService
}
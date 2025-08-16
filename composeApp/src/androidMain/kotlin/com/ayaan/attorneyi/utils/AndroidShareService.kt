package com.ayaan.attorneyi.utils

import android.content.Intent
import android.util.Log

class AndroidShareService() : ShareService {
    override fun share(title: String, url: String) {
        try {
            val context = AppContext.get()
            if (context == null) {
                Log.e("AndroidShareService", "Context is null, cannot share")
                return
            }

            val shareText = if (url.isNotBlank()) {
                "$title\n\n$url"
            } else {
                title
            }

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TEXT, shareText)
            }

            val chooserIntent = Intent.createChooser(shareIntent, "Share via").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            Log.e("AndroidShareService", "Failed to share content", e)
        }
    }
}

actual class ShareServiceProvider {
    actual fun getShareService(): ShareService = AndroidShareService()
}
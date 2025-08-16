package com.ayaan.attorneyi.utils

import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

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
    override fun shareWithImage(title: String, url: String, imagePath: String) {
        val context = AppContext.get()
        val imageFile = File(imagePath)

        if (!imageFile.exists()) {
            // Fallback to text-only sharing if image doesn't exist
            share(title, url)
            return
        }

        val imageUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", // Make sure you have this configured in manifest
            imageFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, "$title\n$url")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share via").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}

actual class ShareServiceProvider {
    actual fun getShareService(): ShareService = AndroidShareService()
}
package com.ayaan.attorneyi.utils

import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class AndroidShareService() : ShareService {
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun share(title: String, url: String) {
        try {
            val context = AppContext.get()
            if(context is Exception || context == null) {
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
            Log.d("AndroidShareService", "Image file does not exist at path: $imagePath")
            // Fallback to text-only sharing if image doesn't exist
            share(title, url)
            return
        }

        try {
            val imageUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
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
        } catch (e: Exception) {
            Log.e("AndroidShareService", "Failed to share with image", e)
            // Fallback to text-only sharing
            share(title, url)
        }
    }

    override fun shareWithImageUrl(
        title: String,
        url: String,
        imageUrl: String,
        callback: (Boolean) -> Unit
    ) {
        if (imageUrl.isBlank()) {
            callback(false)
            share(title, url)
            return
        }

        serviceScope.launch {
            try {
                val context = AppContext.get()
                val cacheDir = File(context.cacheDir, "shared_images")
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs()
                }

                // Generate filename from URL
                val fileName = "shared_image_${System.currentTimeMillis()}.jpg"
                val imageFile = File(cacheDir, fileName)

                // Download the image
                val success = downloadImage(imageUrl, imageFile)

                withContext(Dispatchers.Main) {
                    if (success && imageFile.exists()) {
                        Log.d(
                            "AndroidShareService",
                            "Image downloaded successfully: ${imageFile.absolutePath}"
                        )
                        shareWithImage(title, url, imageFile.absolutePath)
                        callback(true)
                    } else {
                        Log.e("AndroidShareService", "Failed to download image from: $imageUrl")
                        share(title, url)
                        callback(false)
                    }
                }
            } catch (e: Exception) {
                Log.e("AndroidShareService", "Error in shareWithImageUrl", e)
                withContext(Dispatchers.Main) {
                    share(title, url)
                    callback(false)
                }
            }
        }
    }
}
private suspend fun downloadImage(imageUrl: String, outputFile: File): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            url.openStream().use { input ->
                FileOutputStream(outputFile).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: IOException) {
            Log.e("AndroidShareService", "Failed to download image", e)
            false
        } catch (e: Exception) {
            Log.e("AndroidShareService", "Unexpected error downloading image", e)
            false
        }
    }
}

actual class ShareServiceProvider {
    actual fun getShareService(): ShareService = AndroidShareService()
}
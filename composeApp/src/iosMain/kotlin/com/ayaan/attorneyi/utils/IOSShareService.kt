package com.ayaan.attorneyi.utils

import kotlinx.cinterop.BetaInteropApi
import platform.UIKit.*
import platform.Foundation.*
import kotlinx.coroutines.*

class IOSShareService : ShareService {
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    @OptIn(BetaInteropApi::class)
    override fun share(title: String, url: String) {
        val activityItems = listOf(
            NSString.create(string = "$title\n$url")
        )
        val activityViewController = UIActivityViewController(
            activityItems = activityItems,
            applicationActivities = null
        )

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }

    @OptIn(BetaInteropApi::class)
    override fun shareWithImage(title: String, url: String, imagePath: String) {
        val fileManager = NSFileManager.defaultManager

        if (!fileManager.fileExistsAtPath(imagePath)) {
            share(title, url)
            return
        }

        val image = UIImage.imageWithContentsOfFile(imagePath)
        val activityItems = mutableListOf<Any>()

        image?.let { activityItems.add(it) }
        activityItems.add(NSString.create(string = "$title\n$url"))

        val activityViewController = UIActivityViewController(
            activityItems = activityItems,
            applicationActivities = null
        )

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }

    @OptIn(BetaInteropApi::class)
    override fun shareWithImageUrl(title: String, url: String, imageUrl: String, callback: (Boolean) -> Unit) {
        if (imageUrl.isBlank()) {
            callback(false)
            share(title, url)
            return
        }

        serviceScope.launch {
            try {
                val nsUrl = NSURL.URLWithString(imageUrl)
                val data = NSData.dataWithContentsOfURL(nsUrl ?: return@launch)
                val image = UIImage.imageWithData(data ?: return@launch)

                if (image != null) {
                    val activityItems = listOf(image, NSString.create(string = "$title\n$url"))
                    val activityViewController = UIActivityViewController(
                        activityItems = activityItems,
                        applicationActivities = null
                    )

                    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
                    rootViewController?.presentViewController(
                        activityViewController,
                        animated = true,
                        completion = null
                    )
                    callback(true)
                } else {
                    share(title, url)
                    callback(false)
                }
            } catch (e: Exception) {
                share(title, url)
                callback(false)
                e.printStackTrace()
            }
        }
    }
}
actual class ShareServiceProvider {
    actual fun getShareService(): ShareService = IOSShareService()
}
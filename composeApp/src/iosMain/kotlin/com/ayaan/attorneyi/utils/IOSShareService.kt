package com.ayaan.attorneyi.utils

import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage

class IOSShareService : ShareService {
    @OptIn(BetaInteropApi::class)
    override fun share(title: String, url: String) {
        val activityItems = listOf(
            NSString.create(string = "$title\n$url")
        )
        val activityViewController =
            UIActivityViewController(activityItems = activityItems, applicationActivities = null)

        // Get the top-most view controller to present the activity view controller
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(activityViewController, animated = true, completion = null)
    }
    override fun shareWithImage(title: String, url: String, imagePath: String) {
        val fileManager = NSFileManager.defaultManager
        val imageUrl = NSURL.fileURLWithPath(imagePath)

        if (!fileManager.fileExistsAtPath(imagePath)) {
            // Fallback to text-only sharing if image doesn't exist
            share(title, url)
            return
        }

        val image = UIImage.imageWithContentsOfFile(imagePath)
        val activityItems = mutableListOf<Any>()

        // Add image if it exists
        image?.let { activityItems.add(it) }

        // Add text content
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
}
actual class ShareServiceProvider {
    actual fun getShareService(): ShareService = IOSShareService()
}
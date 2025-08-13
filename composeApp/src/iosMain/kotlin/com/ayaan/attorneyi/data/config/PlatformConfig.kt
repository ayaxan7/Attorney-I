package com.ayaan.attorneyi.data.config

import platform.Foundation.NSBundle

actual object PlatformConfig {
    actual fun getApiKey(): String {
        // Read API key from iOS Info.plist (iOS equivalent of BuildConfig)
        val bundle = NSBundle.mainBundle
        return bundle.objectForInfoDictionaryKey("GNewsAPIKey") as? String
            ?: throw IllegalStateException("GNewsAPIKey not found in Info.plist. Please check your iOS configuration.")
    }
}

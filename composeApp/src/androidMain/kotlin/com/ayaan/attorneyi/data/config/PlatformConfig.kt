package com.ayaan.attorneyi.data.config

import com.ayaan.attorneyi.BuildConfig

actual object PlatformConfig {
    actual fun getApiKey(): String {
        return BuildConfig.GNEWS_API_KEY
    }
}

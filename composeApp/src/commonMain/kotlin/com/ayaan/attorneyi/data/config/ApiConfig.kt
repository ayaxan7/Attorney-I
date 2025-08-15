package com.ayaan.attorneyi.data.config

object ApiConfig {
    // Your custom legal news API endpoint
    const val BASE_URL = "https://attorney-i.onrender.com/api"

    // Keep GNews config for reference (your backend uses this)
    const val GNEWS_BASE_URL = "https://gnews.io/api/v4"
    const val SEARCH_QUERY = "\"Supreme Court India\" OR \"High Court\" OR judiciary OR \"constitutional amendment\" OR \"court ruling\" OR \"PIL petition\" OR \"landmark judgment\" OR \"writ petition\" OR \"Chief Justice of India\""

    // Get API key from platform-specific BuildConfig (your backend needs this)
    val GNEWS_API_KEY: String
        get() = PlatformConfig.getApiKey()

    // Available legal tags for filtering
    object LegalTags {
        const val CORPORATE = "Corporate"
        const val CRIMINAL = "Criminal"
        const val INTERNATIONAL = "International"
        const val PRIVACY = "Privacy"
    }
}

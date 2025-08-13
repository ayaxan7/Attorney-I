package com.ayaan.attorneyi.data.config

object ApiConfig {
    const val BASE_URL = "https://gnews.io/api/v4"
    const val SEARCH_QUERY = "\"Supreme Court India\" OR \"High Court\" OR judiciary OR \"constitutional amendment\" OR \"court ruling\" OR \"PIL petition\" OR \"landmark judgment\" OR \"writ petition\" OR \"Chief Justice of India\""

    // Get API key from platform-specific BuildConfig
    val GNEWS_API_KEY: String
        get() = PlatformConfig.getApiKey()

    // Alternative queries you can try if the current one causes issues:
    // const val SEARCH_QUERY = "Supreme Court India judiciary legal news court ruling"
    // const val SEARCH_QUERY = "Supreme Court OR High Court"
    // const val SEARCH_QUERY = "judiciary India"
    // const val SEARCH_QUERY = "legal news India"
}

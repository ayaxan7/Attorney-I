package com.ayaan.attorneyi.data.config

object ApiConfig {
    // TODO: Replace with your actual GNews API key
    // Get your API key from: https://gnews.io/
    const val GNEWS_API_KEY = ""

    const val BASE_URL = "https://gnews.io/api/v4"
    const val SEARCH_QUERY = "\"Supreme Court India\" OR \"High Court\" OR judiciary OR \"constitutional amendment\" OR \"court ruling\" OR \"PIL petition\" OR \"landmark judgment\" OR \"writ petition\" OR \"Chief Justice of India\""

    // Simplified query that's less likely to cause 400 errors
    // Using simpler terms without complex quotes and operators
//    const val SEARCH_QUERY = "Supreme Court India judiciary legal news court ruling"

    // Alternative queries you can try:
    // const val SEARCH_QUERY = "Supreme Court OR High Court"
    // const val SEARCH_QUERY = "judiciary India"
    // const val SEARCH_QUERY = "legal news India"
}

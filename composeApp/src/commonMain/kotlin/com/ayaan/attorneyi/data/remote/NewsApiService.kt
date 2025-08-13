package com.ayaan.attorneyi.data.remote

import com.ayaan.attorneyi.data.config.ApiConfig
import com.ayaan.attorneyi.data.model.NewsResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class NewsApiService(
    private val httpClient: HttpClient
) {
    suspend fun searchNews(
        page: Int = 1,
        maxResults: Int = 10
    ): Result<NewsResponse> {
        return try {
            val response = httpClient.get("${ApiConfig.BASE_URL}/search") {
                parameter("q", ApiConfig.SEARCH_QUERY)
                parameter("country", "in")
                parameter("lang", "en")
                parameter("sortby", "publishedAt")
                parameter("max", maxResults)
                parameter("page", page)
                parameter("apikey", ApiConfig.GNEWS_API_KEY)
            }

            if (response.status.isSuccess()) {
                val newsResponse = response.body<NewsResponse>()
                Result.success(newsResponse)
            } else {
                Result.failure(Exception("API call failed with status: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

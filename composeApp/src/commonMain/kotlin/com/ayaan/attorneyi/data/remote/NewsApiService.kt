package com.ayaan.attorneyi.data.remote

import com.ayaan.attorneyi.data.config.ApiConfig
import com.ayaan.attorneyi.data.model.NewsResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class NewsApiService(
    private val httpClient: HttpClient
) {
    suspend fun searchNews(
        page: Int = 1,
        maxResults: Int = 10
    ): Result<NewsResponse> {
        return try {
            println("Making API request with query: ${ApiConfig.SEARCH_QUERY}")

            val response = httpClient.get("${ApiConfig.BASE_URL}/search") {
                parameter("q", ApiConfig.SEARCH_QUERY)
                parameter("country", "in")
                parameter("lang", "en")
                parameter("sortby", "publishedAt")
                parameter("max", maxResults.coerceIn(1, 100)) // Ensure max is within valid range
                parameter("page", page.coerceAtLeast(1)) // Ensure page is at least 1
                parameter("apikey", ApiConfig.GNEWS_API_KEY)
            }

            when {
                response.status.isSuccess() -> {
                    val newsResponse = response.body<NewsResponse>()
                    Result.success(newsResponse)
                }
                response.status == HttpStatusCode.BadRequest -> {
                    val errorBody = response.bodyAsText()
                    println("API Error (400): $errorBody")
                    Result.failure(Exception("Bad Request: Please check API parameters. Error: $errorBody"))
                }
                response.status == HttpStatusCode.Unauthorized -> {
                    Result.failure(Exception("Unauthorized: Please check your API key"))
                }
                response.status == HttpStatusCode.TooManyRequests -> {
                    Result.failure(Exception("Rate limit exceeded: Please try again later"))
                }
                else -> {
                    val errorBody = response.bodyAsText()
                    println("API Error (${response.status}): $errorBody")
                    Result.failure(Exception("API call failed with status: ${response.status}. Error: $errorBody"))
                }
            }
        } catch (e: Exception) {
            println("Exception in searchNews: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

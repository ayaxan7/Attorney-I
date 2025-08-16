package com.ayaan.attorneyi.data.remote

import com.ayaan.attorneyi.data.config.ApiConfig
import com.ayaan.attorneyi.data.model.LegalNewsResponse
import com.ayaan.attorneyi.data.model.LegalNewsErrorResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.datetime.Clock
import org.koin.core.annotation.Single

@Single
class NewsApiService(
    private val httpClient: HttpClient
) {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    /**
     * Get latest legal news articles (10 most recent)
     * Corresponds to: GET /api/news
     */
    suspend fun getLatestNews(): Result<LegalNewsResponse> {
        return try {
            val timestamp = Clock.System.now().epochSeconds
            println("Fetching latest legal news from: ${ApiConfig.BASE_URL}/news (timestamp: $timestamp)")

            val response = httpClient.get(ApiConfig.NEWS_ENDPOINT) {
                // Add cache-busting headers to ensure fresh data
                header("Cache-Control", "no-cache, no-store, must-revalidate")
                header("Pragma", "no-cache")
                header("Expires", "0")
                // Add timestamp parameter to ensure fresh requests
                parameter("_t", timestamp)
            }

            when {
                response.status.isSuccess() -> {
                    val newsResponse = response.body<LegalNewsResponse>()
                    println("Successfully fetched ${newsResponse.data.articles.size} articles")
                    Result.success(newsResponse)
                }
                response.status == HttpStatusCode.InternalServerError -> {
                    val errorBody = response.bodyAsText()
                    println("Server Error (500): $errorBody")
                    try {
                        val errorResponse = json.decodeFromString<LegalNewsErrorResponse>(errorBody)
                        Result.failure(Exception("Server error: ${errorResponse.message}"))
                    } catch (e: Exception) {
                        Result.failure(Exception("Server error occurred"))
                    }
                }
                else -> {
                    val errorBody = response.bodyAsText()
                    println("API Error (${response.status}): $errorBody")
                    Result.failure(Exception("API call failed with status: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            println("Exception in getLatestNews: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Get legal news articles filtered by tag
     * Corresponds to: GET /api/news?tag=Corporate
     *
     * @param tag Legal tag to filter by (Corporate, Criminal, International, Privacy)
     */
    suspend fun getNewsByTag(tag: String): Result<LegalNewsResponse> {
        return try {
            val timestamp = Clock.System.now().epochSeconds
            println("Fetching legal news by tag '$tag' from: ${ApiConfig.BASE_URL}/news (timestamp: $timestamp)")

            val response = httpClient.get("${ApiConfig.BASE_URL}/news") {
                parameter("tag", tag)
                // Add cache-busting headers to ensure fresh data
                header("Cache-Control", "no-cache, no-store, must-revalidate")
                header("Pragma", "no-cache")
                header("Expires", "0")
                // Add timestamp parameter to ensure fresh requests
                parameter("_t", timestamp)
            }

            when {
                response.status.isSuccess() -> {
                    val newsResponse = response.body<LegalNewsResponse>()
                    println("Successfully fetched ${newsResponse.data.articles.size} articles for tag '$tag'")
                    Result.success(newsResponse)
                }
                response.status == HttpStatusCode.InternalServerError -> {
                    val errorBody = response.bodyAsText()
                    println("Server Error (500): $errorBody")
                    try {
                        val errorResponse = json.decodeFromString<LegalNewsErrorResponse>(errorBody)
                        Result.failure(Exception("Server error: ${errorResponse.message}"))
                    } catch (e: Exception) {
                        Result.failure(Exception("Server error occurred"))
                    }
                }
                else -> {
                    val errorBody = response.bodyAsText()
                    println("API Error (${response.status}): $errorBody")
                    Result.failure(Exception("API call failed with status: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            println("Exception in getNewsByTag: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

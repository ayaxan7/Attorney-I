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
            println("Fetching latest legal news from: ${ApiConfig.BASE_URL}/news")

            val response = httpClient.get("${ApiConfig.BASE_URL}/news")

            when {
                response.status.isSuccess() -> {
                    val newsResponse = response.body<LegalNewsResponse>()
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
            println("Fetching legal news by tag '$tag' from: ${ApiConfig.BASE_URL}/news")

            val response = httpClient.get("${ApiConfig.BASE_URL}/news") {
                parameter("tag", tag)
            }

            when {
                response.status.isSuccess() -> {
                    val newsResponse = response.body<LegalNewsResponse>()
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

    // Keep the old method for backward compatibility during transition
    suspend fun searchNews(
        page: Int = 1,
        maxResults: Int = 10
    ): Result<LegalNewsResponse> {
        // Redirect to new API
        return getLatestNews()
    }
}

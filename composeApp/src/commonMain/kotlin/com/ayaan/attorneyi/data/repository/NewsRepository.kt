package com.ayaan.attorneyi.data.repository

import com.ayaan.attorneyi.data.model.LegalArticle
import com.ayaan.attorneyi.data.remote.NewsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class NewsRepository(
    private val apiService: NewsApiService
) {
    /**
     * Get the latest legal news articles (10 most recent)
     * Each call creates a fresh flow that makes a new API request
     */
    fun getLatestNews(): Flow<Result<List<LegalArticle>>> = flow {
        try {
            println("NewsRepository: Making fresh API call for latest news")
            val result = apiService.getLatestNews()
            result.fold(
                onSuccess = { newsResponse ->
                    println("NewsRepository: Successfully fetched ${newsResponse.data.articles.size} articles")
                    emit(Result.success(newsResponse.data.articles))
                },
                onFailure = { exception ->
                    println("NewsRepository: API call failed - ${exception.message}")
                    emit(Result.failure(exception))
                }
            )
        } catch (e: Exception) {
            println("NewsRepository: Exception during API call - ${e.message}")
            emit(Result.failure(e))
        }
    }

    /**
     * Get legal news articles filtered by tag
     * Each call creates a fresh flow that makes a new API request
     * @param tag Legal category tag (Corporate, Criminal, International, Privacy)
     */
    fun getNewsByTag(tag: String): Flow<Result<List<LegalArticle>>> = flow {
        try {
            println("NewsRepository: Making fresh API call for tag '$tag'")
            val result = apiService.getNewsByTag(tag)
            result.fold(
                onSuccess = { newsResponse ->
                    println("NewsRepository: Successfully fetched ${newsResponse.data.articles.size} articles for tag '$tag'")
                    emit(Result.success(newsResponse.data.articles))
                },
                onFailure = { exception ->
                    println("NewsRepository: API call failed for tag '$tag' - ${exception.message}")
                    emit(Result.failure(exception))
                }
            )
        } catch (e: Exception) {
            println("NewsRepository: Exception during API call for tag '$tag' - ${e.message}")
            emit(Result.failure(e))
        }
    }

    /**
     * Force refresh by making a new API call
     * This method ensures a completely fresh request every time
     */
    suspend fun refreshLatestNews(): Result<List<LegalArticle>> {
        return try {
            println("NewsRepository: Force refreshing latest news")
            val result = apiService.getLatestNews()
            result.fold(
                onSuccess = { newsResponse ->
                    println("NewsRepository: Force refresh successful - ${newsResponse.data.articles.size} articles")
                    Result.success(newsResponse.data.articles)
                },
                onFailure = { exception ->
                    println("NewsRepository: Force refresh failed - ${exception.message}")
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            println("NewsRepository: Force refresh exception - ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Force refresh by tag
     */
    suspend fun refreshNewsByTag(tag: String): Result<List<LegalArticle>> {
        return try {
            println("NewsRepository: Force refreshing news for tag '$tag'")
            val result = apiService.getNewsByTag(tag)
            result.fold(
                onSuccess = { newsResponse ->
                    println("NewsRepository: Force refresh successful for tag '$tag' - ${newsResponse.data.articles.size} articles")
                    Result.success(newsResponse.data.articles)
                },
                onFailure = { exception ->
                    println("NewsRepository: Force refresh failed for tag '$tag' - ${exception.message}")
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            println("NewsRepository: Force refresh exception for tag '$tag' - ${e.message}")
            Result.failure(e)
        }
    }

    // Keep the old method for backward compatibility during transition
    fun getNews(page: Int = 1, maxResults: Int = 10): Flow<Result<List<LegalArticle>>> = flow {
        try {
            val result = apiService.getLatestNews()
            result.fold(
                onSuccess = { newsResponse ->
                    emit(Result.success(newsResponse.data.articles))
                },
                onFailure = { exception ->
                    emit(Result.failure(exception))
                }
            )
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

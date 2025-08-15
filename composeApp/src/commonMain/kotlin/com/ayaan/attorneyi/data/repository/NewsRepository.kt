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
     */
    fun getLatestNews(): Flow<Result<List<LegalArticle>>> = flow {
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

    /**
     * Get legal news articles filtered by tag
     * @param tag Legal category tag (Corporate, Criminal, International, Privacy)
     */
    fun getNewsByTag(tag: String): Flow<Result<List<LegalArticle>>> = flow {
        try {
            val result = apiService.getNewsByTag(tag)
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

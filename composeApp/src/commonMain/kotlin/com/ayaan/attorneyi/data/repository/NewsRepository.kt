package com.ayaan.attorneyi.data.repository

import com.ayaan.attorneyi.data.model.Article
import com.ayaan.attorneyi.data.remote.NewsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepository(
    private val apiService: NewsApiService
) {
    fun getNews(page: Int = 1, maxResults: Int = 10): Flow<Result<List<Article>>> = flow {
        try {
            val result = apiService.searchNews(page, maxResults)
            result.fold(
                onSuccess = { newsResponse ->
                    emit(Result.success(newsResponse.articles))
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

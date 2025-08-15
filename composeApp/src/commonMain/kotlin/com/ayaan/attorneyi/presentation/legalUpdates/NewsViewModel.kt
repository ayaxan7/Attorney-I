package com.ayaan.attorneyi.presentation.legalUpdates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayaan.attorneyi.AppLogger
import com.ayaan.attorneyi.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        loadLatestNews()
    }

    /**
     * Load latest legal news articles
     */
    fun loadLatestNews() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                selectedTag = null
            )

            newsRepository.getLatestNews().collect { result ->
                result.fold(
                    onSuccess = { articles ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            articles = articles,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                        AppLogger.d("NewsViewModel", "Error loading news: ${exception.message}")
                        exception.printStackTrace()
                    }
                )
            }
        }
    }

    /**
     * Load news articles filtered by legal tag
     */
    fun loadNewsByTag(tag: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                selectedTag = tag
            )

            newsRepository.getNewsByTag(tag).collect { result ->
                result.fold(
                    onSuccess = { articles ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            articles = articles,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                        AppLogger.d("NewsViewModel", "Error loading news by tag '$tag': ${exception.message}")
                        exception.printStackTrace()
                    }
                )
            }
        }
    }

    /**
     * Clear tag filter and show all latest news
     */
    fun clearTagFilter() {
        loadLatestNews()
    }

    // Keep old methods for backward compatibility
    fun loadNews() {
        loadLatestNews()
    }

    fun retry() {
        if (_uiState.value.selectedTag != null) {
            loadNewsByTag(_uiState.value.selectedTag!!)
        } else {
            loadLatestNews()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            val currentTag = _uiState.value.selectedTag
            val repository = if (currentTag != null) {
                newsRepository.getNewsByTag(currentTag)
            } else {
                newsRepository.getLatestNews()
            }

            repository.collect { result ->
                result.fold(
                    onSuccess = { articles ->
                        _uiState.value = _uiState.value.copy(
                            isRefreshing = false,
                            articles = articles,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isRefreshing = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    }
                )
            }
        }
    }
}
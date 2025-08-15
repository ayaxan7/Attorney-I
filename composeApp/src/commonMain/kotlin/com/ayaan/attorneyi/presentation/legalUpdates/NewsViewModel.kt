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
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            newsRepository.getNews().collect { result ->
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

    fun retry() {
        loadNews()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            newsRepository.getNews().collect { result ->
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
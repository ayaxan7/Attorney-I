package com.ayaan.attorneyi.presentation.legalUpdates

import com.ayaan.attorneyi.data.model.Article

data class NewsUiState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
)

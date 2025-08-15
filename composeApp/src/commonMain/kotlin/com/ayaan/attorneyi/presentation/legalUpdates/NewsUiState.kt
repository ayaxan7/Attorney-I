package com.ayaan.attorneyi.presentation.legalUpdates

import com.ayaan.attorneyi.data.model.LegalArticle

data class NewsUiState(
    val isLoading: Boolean = false,
    val articles: List<LegalArticle> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val selectedTag: String? = null,
    val availableTags: List<String> = listOf("Corporate", "Criminal", "International", "Privacy")
)

package com.ayaan.attorneyi.presentation.legalUpdates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayaan.attorneyi.AppLogger
import com.ayaan.attorneyi.data.repository.NewsRepository
import com.ayaan.attorneyi.presentation.legalUpdates.state.NewsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.koin.core.annotation.Single

@Single
class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

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

    /**
     * Toggle search mode on/off
     */
    fun toggleSearch() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            isSearchActive = !currentState.isSearchActive,
            searchQuery = if (!currentState.isSearchActive) "" else currentState.searchQuery,
            filteredArticles = if (!currentState.isSearchActive) emptyList() else currentState.filteredArticles
        )
    }

    /**
     * Update search query and perform search
     */
    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        // Cancel previous search job
        searchJob?.cancel()

        // Debounce search to avoid too many calls
        searchJob = viewModelScope.launch {
            delay(300) // Wait 300ms before searching
            performSearch(query)
        }
    }

    /**
     * Perform local search on articles
     */
    private fun performSearch(query: String) {
        val currentArticles = _uiState.value.articles

        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(
                filteredArticles = emptyList(),
                isSearching = false
            )
            return
        }

        _uiState.value = _uiState.value.copy(isSearching = true)

        val filteredArticles = currentArticles.filter { article ->
            val queryLower = query.lowercase()
            article.title.lowercase().contains(queryLower) ||
                    article.description?.lowercase()?.contains(queryLower) == true ||
                    article.tags.any { tag -> tag.lowercase().contains(queryLower) } ||
                    article.source.name.lowercase().contains(queryLower)
        }

        _uiState.value = _uiState.value.copy(
            filteredArticles = filteredArticles,
            isSearching = false
        )
    }

    /**
     * Clear search and return to normal view
     */
    fun clearSearch() {
        searchJob?.cancel()
        _uiState.value = _uiState.value.copy(
            isSearchActive = false,
            searchQuery = "",
            filteredArticles = emptyList(),
            isSearching = false
        )
    }

    /**
     * Get current articles to display (either filtered or normal)
     */
    fun getCurrentArticles(): List<com.ayaan.attorneyi.data.model.LegalArticle> {
        val state = _uiState.value
        return if (state.isSearchActive && state.searchQuery.isNotBlank()) {
            state.filteredArticles
        } else {
            state.articles
        }
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
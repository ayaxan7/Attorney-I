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

            try {
                newsRepository.getLatestNews().collect { result ->
                    result.fold(
                        onSuccess = { articles ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                articles = articles,
                                error = null,
                                // Clear any existing filtered articles to force refresh
                                filteredArticles = emptyList()
                            )
                            AppLogger.d("NewsViewModel", "Latest news loaded: ${articles.size} articles")
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
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load news"
                )
                AppLogger.d("NewsViewModel", "Exception in loadLatestNews: ${e.message}")
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

            try {
                newsRepository.getNewsByTag(tag).collect { result ->
                    result.fold(
                        onSuccess = { articles ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                articles = articles,
                                error = null,
                                // Clear any existing filtered articles to force refresh
                                filteredArticles = emptyList()
                            )
                            AppLogger.d("NewsViewModel", "News loaded for tag '$tag': ${articles.size} articles")
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
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load news for tag: $tag"
                )
                AppLogger.d("NewsViewModel", "Exception in loadNewsByTag: ${e.message}")
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
    fun retry() {
        if (_uiState.value.selectedTag != null) {
            loadNewsByTag(_uiState.value.selectedTag!!)
        } else {
            loadLatestNews()
        }
    }

    /**
     * Enhanced refresh function to ensure fresh API calls with immediate list updates
     */
    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            try {
                val currentTag = _uiState.value.selectedTag
                val result = if (currentTag != null) {
                    newsRepository.refreshNewsByTag(currentTag)
                } else {
                    newsRepository.refreshLatestNews()
                }

                result.fold(
                    onSuccess = { articles ->
                        _uiState.value = _uiState.value.copy(
                            isRefreshing = false,
                            articles = articles,
                            error = null,
                            // Clear filtered articles to ensure fresh display
                            filteredArticles = emptyList()
                        )
                        AppLogger.d("NewsViewModel", "Refresh successful: ${articles.size} articles loaded")

                        // If we're in search mode, re-apply the search to the new data
                        if (_uiState.value.isSearchActive && _uiState.value.searchQuery.isNotBlank()) {
                            performSearch(_uiState.value.searchQuery)
                        }
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isRefreshing = false,
                            error = exception.message ?: "Failed to refresh news"
                        )
                        AppLogger.d("NewsViewModel", "Refresh failed: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    error = e.message ?: "Failed to refresh news"
                )
                AppLogger.d("NewsViewModel", "Refresh exception: ${e.message}")
            }
        }
    }
}
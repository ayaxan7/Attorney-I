package com.ayaan.attorneyi.presentation.legalUpdates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ayaan.attorneyi.utils.AppLogger
import com.ayaan.attorneyi.presentation.legalUpdates.components.BreakingNewsToggle
import com.ayaan.attorneyi.presentation.legalUpdates.components.HeaderSection
import com.ayaan.attorneyi.presentation.legalUpdates.components.LegalCategoryFilters
import com.ayaan.attorneyi.presentation.legalUpdates.components.NewsList
import com.ayaan.attorneyi.presentation.legalUpdates.components.NoSearchResults
import com.ayaan.attorneyi.presentation.legalUpdates.state.ErrorState
import com.ayaan.attorneyi.presentation.legalUpdates.state.LoadingState
import com.ayaan.attorneyi.presentation.legalUpdates.state.NewsUiState
import com.ayaan.attorneyi.presentation.ui.DarkBackground
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewsScreen(
    modifier: Modifier = Modifier, viewModel: NewsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    AppLogger.d("NewsScreen", "Current UI State: $uiState")

    BoxWithConstraints(
        modifier = modifier.fillMaxSize().background(DarkBackground)
    ) {
        val isLandscape = maxWidth > maxHeight
        val contentPadding = if (isLandscape) 24.dp else 16.dp

        NewsContent(
            uiState = uiState,
            onRetry = viewModel::retry,
            onRefresh = viewModel::refresh,
            onTagSelected = viewModel::loadNewsByTag,
            onClearFilter = viewModel::clearTagFilter,
            onSearchToggle = viewModel::toggleSearch,
            onSearchQueryChange = viewModel::updateSearchQuery,
            onSearchClear = viewModel::clearSearch,
            contentPadding = contentPadding,
            isLandscape = isLandscape
        )
    }
}

@Composable
private fun NewsContent(
    uiState: NewsUiState,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onTagSelected: (String) -> Unit,
    onClearFilter: () -> Unit,
    onSearchToggle: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchClear: () -> Unit,
    contentPadding: Dp,
    isLandscape: Boolean
) {
    Column {
        // Header with search functionality
        HeaderSection(
            isSearchActive = uiState.isSearchActive,
            searchQuery = uiState.searchQuery,
            onSearchToggle = onSearchToggle,
            onSearchQueryChange = onSearchQueryChange,
            onSearchClear = onSearchClear
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = contentPadding)
        ) {
            // Show search results info if searching
//            if (uiState.isSearchActive && uiState.searchQuery.isNotBlank()) {
//                SearchResultsHeader(
//                    query = uiState.searchQuery,
//                    resultCount = uiState.filteredArticles.size,
//                    isSearching = uiState.isSearching
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//            }

            // Only show filters when not in search mode
            if (!uiState.isSearchActive) {
                // Legal category filters with tag functionality
                LegalCategoryFilters(
                    availableTags = uiState.availableTags,
                    selectedTag = uiState.selectedTag,
                    onTagSelected = onTagSelected,
                    onClearFilter = onClearFilter
                )

                // Breaking news toggle
                BreakingNewsToggle()

                Spacer(modifier = Modifier.height(16.dp))
            }

            when {
                uiState.isLoading -> {
                    LoadingState()
                }

                // Show NoSearchResults when actively searching with no results (and no error)
                uiState.isSearchActive && uiState.searchQuery.isNotBlank() && uiState.filteredArticles.isEmpty() && !uiState.isSearching && uiState.error == null -> {
                    NoSearchResults(query = uiState.searchQuery)
                }

                // Show error state only when there's an actual error
                // This includes: network errors, API errors, etc. but NOT empty search results
                uiState.error != null && (!uiState.isSearchActive || (uiState.isSearchActive && uiState.articles.isEmpty())) -> {
                    ErrorState(
                        error = uiState.error,
                        onRetry = onRetry
                    )
                    AppLogger.d("NewsScreen", "Error state with message: ${uiState.error}")
                }

                else -> {
                    // Use the existing NewsList component with current articles
                    NewsList(
                        articles = getCurrentDisplayArticles(uiState),
                        onRefresh = onRefresh,
                        isRefreshing = uiState.isRefreshing,
                        isLandscape = isLandscape
                    )
                }
            }
        }
    }
}

// Helper function to get current articles to display
private fun getCurrentDisplayArticles(uiState: NewsUiState): List<com.ayaan.attorneyi.data.model.LegalArticle> {
    return if (uiState.isSearchActive && uiState.searchQuery.isNotBlank()) {
        uiState.filteredArticles
    } else {
        uiState.articles
    }
}

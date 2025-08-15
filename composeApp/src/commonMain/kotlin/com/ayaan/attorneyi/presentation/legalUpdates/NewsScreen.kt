package com.ayaan.attorneyi.presentation.legalUpdates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ayaan.attorneyi.AppLogger
import com.ayaan.attorneyi.presentation.legalUpdates.components.BreakingNewsToggle
import com.ayaan.attorneyi.presentation.legalUpdates.components.HeaderSection
import com.ayaan.attorneyi.presentation.legalUpdates.components.NewsList
import org.koin.compose.viewmodel.koinViewModel
import com.ayaan.attorneyi.presentation.ui.DarkBackground
import com.ayaan.attorneyi.presentation.legalUpdates.state.ErrorState
import com.ayaan.attorneyi.presentation.legalUpdates.state.LoadingState

@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    AppLogger.d("NewsScreen", "Current UI State: $uiState")

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
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
    Column() {
        // Header with search functionality
        HeaderSection(
            isSearchActive = uiState.isSearchActive,
            searchQuery = uiState.searchQuery,
            onSearchToggle = onSearchToggle,
            onSearchQueryChange = onSearchQueryChange,
            onSearchClear = onSearchClear
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = contentPadding)
        ) {
            // Show search results info if searching
            if (uiState.isSearchActive && uiState.searchQuery.isNotBlank()) {
                SearchResultsHeader(
                    query = uiState.searchQuery,
                    resultCount = uiState.filteredArticles.size,
                    isSearching = uiState.isSearching
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

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

                uiState.error != null && getCurrentDisplayArticles(uiState).isEmpty() -> {
                    ErrorState(
                        error = uiState.error,
                        onRetry = onRetry
                    )
                    AppLogger.d("NewsScreen", "Error state with message: ${uiState.error}")
                }

                uiState.isSearchActive && uiState.searchQuery.isNotBlank() && uiState.filteredArticles.isEmpty() && !uiState.isSearching -> {
                    // No search results
                    NoSearchResults(query = uiState.searchQuery)
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

@Composable
private fun SearchResultsHeader(
    query: String,
    resultCount: Int,
    isSearching: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSearching) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Searching for \"$query\"...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "Found $resultCount result${if (resultCount != 1) "s" else ""} for \"$query\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun NoSearchResults(query: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No Results Found",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "No articles found for \"$query\"",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Try searching with different keywords",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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

@Composable
private fun LegalCategoryFilters(
    availableTags: List<String>,
    selectedTag: String?,
    onTagSelected: (String) -> Unit,
    onClearFilter: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Legal Categories:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (selectedTag != null) {
                TextButton(onClick = onClearFilter) {
                    Text("Clear Filter")
                }
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(availableTags) { tag ->
                FilterChip(
                    onClick = { onTagSelected(tag) },
                    label = { Text(tag) },
                    selected = selectedTag == tag
                )
            }
        }
    }
}

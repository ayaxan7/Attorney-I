package com.ayaan.attorneyi.presentation.legalUpdates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ayaan.attorneyi.AppLogger
import com.ayaan.attorneyi.data.model.LegalArticle
import com.ayaan.attorneyi.presentation.legalUpdates.components.BreakingNewsToggle
import com.ayaan.attorneyi.presentation.legalUpdates.components.HeaderSection
import com.ayaan.attorneyi.presentation.legalUpdates.components.NewsList
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
    contentPadding: Dp,
    isLandscape: Boolean
) {
    Column() {
        HeaderSection()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = contentPadding)
        ) {
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

            when {
                uiState.isLoading -> {
                    LoadingState()
                }

                uiState.error != null && uiState.articles.isEmpty() -> {
                    ErrorState(
                        error = uiState.error,
                        onRetry = onRetry
                    )
                    AppLogger.d("NewsScreen", "Error state with message: ${uiState.error}")
                }

                else -> {
                    // Use the existing NewsList component
                    NewsList(
                        articles = uiState.articles,
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

private fun formatPublishedDate(publishedAt: String): String {
    return try {
        val instant = Instant.parse(publishedAt)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        "${localDateTime.date} ${localDateTime.time.hour}:${localDateTime.time.minute.toString().padStart(2, '0')}"
    } catch (e: Exception) {
        publishedAt
    }
}

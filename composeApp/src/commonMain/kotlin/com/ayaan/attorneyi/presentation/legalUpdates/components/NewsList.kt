package com.ayaan.attorneyi.presentation.legalUpdates.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.ayaan.attorneyi.data.model.Article
import androidx.compose.foundation.lazy.items
@Composable
fun NewsList(
    articles: List<Article>,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    isLandscape: Boolean
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(articles) { article ->
            NewsItem(
                article = article,
                isLandscape = isLandscape
            )
        }
    }
}

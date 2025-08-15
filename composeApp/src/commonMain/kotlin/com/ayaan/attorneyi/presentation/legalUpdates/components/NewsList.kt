package com.ayaan.attorneyi.presentation.legalUpdates.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.ayaan.attorneyi.data.model.LegalArticle
import androidx.compose.foundation.lazy.items

@Composable
fun NewsList(
    articles: List<LegalArticle>,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    isLandscape: Boolean
) {
    val uriHandler = LocalUriHandler.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(articles) { article ->
            NewsItem(
                article = article,
                isLandscape = isLandscape,
                onOpenUrl = { url ->
                    uriHandler.openUri(url)
                }
            )
        }
    }
}

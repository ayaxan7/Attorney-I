package com.ayaan.attorneyi.presentation.legalUpdates.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayaan.attorneyi.data.model.LegalArticle
import com.ayaan.attorneyi.presentation.ui.CardBackground
import com.ayaan.attorneyi.presentation.ui.GoldAccent
import com.ayaan.attorneyi.presentation.ui.TextPrimary
import com.ayaan.attorneyi.presentation.ui.TextSecondary
import com.ayaan.attorneyi.utils.AppLogger
import com.ayaan.attorneyi.utils.ShareServiceProvider
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun NewsItem(
    article: LegalArticle,
    isLandscape: Boolean,
    onOpenUrl: (String) -> Unit
) {
    Card(
        onClick = {
            onOpenUrl(article.url)
        },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Title
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            if (!article.description.isNullOrEmpty()) {
                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bottom row with source, time and actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = article.source.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GoldAccent,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = formatPublishedDate(article.publishedAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
//                    IconButton(onClick = { /* Handle bookmark */ }) {
//                        Icon(
//                            Icons.Default.BookmarkBorder,
//                            contentDescription = "Bookmark",
//                            tint = GoldAccent
//                        )
//                    }
                    IconButton(onClick = {
//                        shareContent(
//                            ShareServiceProvider(),
//                            article.title,
//                            article.url
//                        )
                        shareContentWithImageUrl(
                            ShareServiceProvider(),
                            article.title,
                            article.url,
                            article.image ?: ""
                        )
                    }) {
                        Icon(
                            Icons.Default.Share, contentDescription = "Share", tint = GoldAccent
                        )
                    }
                }
            }
        }
    }
}

private fun formatPublishedDate(publishedAt: String): String {
    return try {
        val instant = Instant.parse(publishedAt)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        "${localDateTime.date} ${localDateTime.time.hour}:${
            localDateTime.time.minute.toString().padStart(2, '0')
        }"
    } catch (e: Exception) {
        publishedAt
    }
}
private fun shareContentWithImageUrl(
    serviceProvider: ShareServiceProvider,
    title: String,
    url: String,
    imageUrl: String
) {
    val shareService = serviceProvider.getShareService()
    shareService.shareWithImageUrl(title, url, imageUrl) { success ->
        if (success) {
            AppLogger.d("ShareService", "Successfully shared with image")
        } else {
            AppLogger.d("ShareService", "Shared without image (fallback)")
        }
    }
}
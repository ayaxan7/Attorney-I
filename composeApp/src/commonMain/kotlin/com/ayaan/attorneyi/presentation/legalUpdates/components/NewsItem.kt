package com.ayaan.attorneyi.presentation.legalUpdates.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayaan.attorneyi.data.model.Article
import com.ayaan.attorneyi.presentation.ui.CardBackground
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.ayaan.attorneyi.presentation.ui.GoldAccent
import com.ayaan.attorneyi.presentation.ui.TextPrimary
import com.ayaan.attorneyi.presentation.ui.TextSecondary

@Composable
fun NewsItem(
    article: Article,
    isLandscape: Boolean
) {
    Card(
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
                maxLines = 3,
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

            // Bottom row with time and actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatPublishedDate(article.publishedAt),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = GoldAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = GoldAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
private fun formatPublishedDate(publishedAt: String): String {
    return try {
        val instant = Instant.parse(publishedAt)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val now = kotlinx.datetime.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val hoursAgo = (now.hour - localDateTime.hour).let {
            if (it < 0) it + 24 else it
        }

        when {
            hoursAgo < 1 -> "Just now"
            hoursAgo == 1 -> "1 hour ago"
            hoursAgo < 24 -> "$hoursAgo hours ago"
            else -> "1 day ago"
        }
    } catch (e: Exception) {
        "Unknown time"
    }
}
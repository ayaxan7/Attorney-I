package com.ayaan.attorneyi.presentation.legalUpdates.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ayaan.attorneyi.presentation.ui.CardBackground
import com.ayaan.attorneyi.presentation.ui.DarkBackground
import com.ayaan.attorneyi.presentation.ui.GoldAccent
import com.ayaan.attorneyi.presentation.ui.TextPrimary
import com.ayaan.attorneyi.presentation.ui.TextSecondary

@Composable
fun LegalCategoryFilters(
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
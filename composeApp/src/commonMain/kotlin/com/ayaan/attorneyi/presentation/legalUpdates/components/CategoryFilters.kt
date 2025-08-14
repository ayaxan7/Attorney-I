package com.ayaan.attorneyi.presentation.legalUpdates.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ayaan.attorneyi.presentation.ui.CardBackground
import com.ayaan.attorneyi.presentation.ui.DarkBackground
import com.ayaan.attorneyi.presentation.ui.GoldAccent
import com.ayaan.attorneyi.presentation.ui.TextPrimary
import com.ayaan.attorneyi.presentation.ui.TextSecondary

@Composable
fun CategoryFilters() {
    val categories = listOf("Corporate", "Criminal", "International", "Constitutional")
    var selectedCategory by remember { mutableStateOf("Corporate") }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                onClick = { selectedCategory = category },
                label = {
                    Text(
                        text = category,
                        color = if (selectedCategory == category) TextPrimary else TextSecondary
                    )
                },
                selected = selectedCategory == category,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GoldAccent,
                    containerColor = CardBackground,
                    selectedLabelColor = DarkBackground,
                    labelColor = TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = Color.Transparent,
                    selectedBorderColor = Color.Transparent,
                    enabled = true,
                    selected = selectedCategory == category,
                    disabledBorderColor = Color.Transparent,
                    disabledSelectedBorderColor = Color.Transparent,
                    borderWidth = 0.dp,
                    selectedBorderWidth = 0.dp
                )
            )
        }
    }
}
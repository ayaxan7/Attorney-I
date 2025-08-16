package com.ayaan.attorneyi.presentation.legalUpdates.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import attorneyi.composeapp.generated.resources.Res
import attorneyi.composeapp.generated.resources.legalupdatesheader
import com.ayaan.attorneyi.presentation.ui.GoldAccent
import com.ayaan.attorneyi.presentation.ui.TextPrimary

@Composable
fun HeaderSection(
    isSearchActive: Boolean = false,
    searchQuery: String = "",
    onSearchToggle: () -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onSearchClear: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            // Background Image from drawable
            Image(
                painter = painterResource(Res.drawable.legalupdatesheader),
                contentDescription = "Legal background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Optional: Add a dark overlay for better text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.6f),
                                Color.Black.copy(alpha = 0.3f)
                            )
                        )
                    )
            )

            // Content on top of the image
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Breaking News",
                    tint = GoldAccent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Breaking Legal Update",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        if (isSearchActive) {
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onSearchClear = onSearchClear,
            )
        } else {
            // Legal Updates title with icons - with padding restored for this section
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Legal Updates",
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = GoldAccent,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onSearchToggle() }
                )
            }
        }
    }
}
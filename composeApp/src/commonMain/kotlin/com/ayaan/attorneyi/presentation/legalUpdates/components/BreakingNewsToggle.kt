package com.ayaan.attorneyi.presentation.legalUpdates.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ayaan.attorneyi.presentation.ui.GoldAccent
import com.ayaan.attorneyi.presentation.ui.TextPrimary

@Composable
fun BreakingNewsToggle() {
    var isEnabled by remember { mutableStateOf(true) }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Enable Breaking News Alerts",
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )
        
        Switch(
            checked = isEnabled,
            onCheckedChange = { isEnabled = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = GoldAccent,
                checkedTrackColor = GoldAccent.copy(alpha = 0.5f),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.DarkGray
            )
        )
    }
}
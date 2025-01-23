package com.mobile.animauxdomestiques.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    isDarkModeEnabled: Boolean,
    fontSize: Float,
    onDarkModeToggle: (Boolean) -> Unit,
    onFontSizeChange: (Float) -> Unit
) {
    var darkMode by remember { mutableStateOf(isDarkModeEnabled) }
    var sliderValue by remember { mutableFloatStateOf(fontSize) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Dark Mode
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Dark Mode")
            Switch(
                checked = darkMode,
                onCheckedChange = {
                    darkMode = it
                    coroutineScope.launch {
                        onDarkModeToggle(it)
                    }
                }
            )
        }
        HorizontalDivider()
        // Font Size
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Font Size: ${sliderValue.toInt()}sp")
            Slider(
                value = sliderValue,
                onValueChange = {
                    sliderValue = it
                    coroutineScope.launch {
                        onFontSizeChange(it)
                    }
                },
                valueRange = 12f..24f,
                steps = 5
            )
        }

        // Preview Text
        Text(
            text = "Preview Text",
            fontSize = sliderValue.sp
        )
    }
}

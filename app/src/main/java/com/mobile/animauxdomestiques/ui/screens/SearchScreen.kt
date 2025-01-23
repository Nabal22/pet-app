package com.mobile.animauxdomestiques.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.TypeSpecimen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.mobile.animauxdomestiques.MainViewModel
import com.mobile.animauxdomestiques.ui.components.SearchBar

@Composable
fun SearchScreen(model: MainViewModel, modifier: Modifier, navHostController: NavHostController) {
    val searchResults by model.searchResultsFlow.collectAsState(initial = emptyList())

    Column (
        modifier = modifier
    ) {
        SearchBar(
            completeList = searchResults,
            placeholder = { Text("Animal, activité ou espèce...") },
            fun(type : String): ImageVector {
                return when (type) {
                    "Animal" -> Icons.Default.Pets
                    "Activity" -> Icons.Default.SportsBaseball
                    "Specie" -> Icons.Default.TypeSpecimen
                    else -> Icons.Default.Settings
                }
            },
            navHostController
        )
    }
}

package com.mobile.animauxdomestiques.ui.components.animal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobile.animauxdomestiques.MainViewModel
import com.mobile.animauxdomestiques.data.entities.Animal
import com.mobile.animauxdomestiques.utils.isPortrait

@Composable
fun AnimalCardGrid(animalList : List<Animal>, model: MainViewModel, navController: NavHostController) {
    Column(
        Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        if (animalList.isNotEmpty()) {
            LazyVerticalGrid(
                columns = if (isPortrait())
                    GridCells.Fixed(2)
                else
                    GridCells.Fixed(4)
                ,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(animalList) { it ->
                    AnimalCard(it, model, navController)
                }
            }
        } else {
            Text(
                text = "Aucun animal pour l'instant..."
            )
        }
    }
}
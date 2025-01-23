package com.mobile.animauxdomestiques.ui.components.animal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobile.animauxdomestiques.MainViewModel
import com.mobile.animauxdomestiques.data.entities.Animal
import com.mobile.animauxdomestiques.navigation.Screen
import com.mobile.animauxdomestiques.ui.components.ImagePicker
import com.mobile.animauxdomestiques.utils.isPortrait

@Composable
fun AnimalCard(animal: Animal, model: MainViewModel, navController: NavHostController) {
    Card(
        onClick = {
            model.setAnimalSelected(animal)
            navController.navigate(Screen.AnimalScreen.route)
        },
        modifier = Modifier
            .then(if (isPortrait())
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            else
                Modifier
                    .height(100.dp)
            ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImagePicker(
                    animal.imagePath,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = animal.name,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize,
                )
            }
        }
    }
}
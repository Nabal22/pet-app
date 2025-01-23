package com.mobile.animauxdomestiques.ui.screens.animal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobile.animauxdomestiques.MainViewModel
import com.mobile.animauxdomestiques.navigation.Screen
import com.mobile.animauxdomestiques.ui.components.activity.ActivityItem
import com.mobile.animauxdomestiques.ui.components.activityconfiguration.ActivityConfigurationForm
import com.mobile.animauxdomestiques.ui.components.animal.AnimalAttributeCard
import com.mobile.animauxdomestiques.ui.components.ImagePicker
import com.mobile.animauxdomestiques.utils.isPortrait

@Composable
fun AnimalScreen(model: MainViewModel, modifier: Modifier, navController: NavHostController, snackbarHostState: SnackbarHostState) {
    val animal = model.animalSelected.value
    val specie by model.getSpecieById(animal?.specieId ?: 0).collectAsState(initial = null)

    val activityList = animal?.let {
        model.getActivityFromAnimal(animalId = it.animalId).collectAsState(listOf()).value
    } ?: emptyList()

    if (animal == null) {
        navController.navigate(Screen.AnimalListScreen.route) {
            popUpTo(Screen.AnimalListScreen.route) { inclusive = true }
        }
        return
    }

    var erreur by model.erreurIns
    var erreurMsg by model.erreurMsg

    LaunchedEffect(erreur) {
        when (erreur){
            1-> snackbarHostState.showSnackbar(
                message = erreurMsg
            )
            2-> snackbarHostState.showSnackbar(
                message = erreurMsg
            )
        }
        erreur = 0
        erreurMsg = ""
    }

    Column(
        modifier = modifier
            .padding(10.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(
                10.dp
            )
    ) {
        Row (
            modifier = Modifier
                .padding(if (isPortrait()) 10.dp else 0.dp)
                .fillMaxWidth(),
        )
        {
            ImagePicker(
                animal.imagePath,
                Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = animal.name,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (animal.breed?.trim()?.isNotEmpty() == true) {
                    Text(
                        text = "Race : ${animal.breed}",
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                specie?.let { loadedSpecie ->
                    Text(
                        text = "Espèce : ${loadedSpecie.name}",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            if (!isPortrait()) {
                Row {
                    AnimalAttributeCard(
                        title = "Genre",
                        data = animal.gender?.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    AnimalAttributeCard(
                        title = "Age",
                        data = animal.age?.toString(),
                        unit = " ans",
                        modifier = Modifier.weight(1f)
                    )
                    AnimalAttributeCard(
                        title = "Poids",
                        data = animal.weight?.toString(),
                        unit = " Kg",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        if (isPortrait()) {
            Row {
                AnimalAttributeCard(
                    title = "Genre",
                    data = animal.gender?.toString(),
                    modifier = Modifier.weight(1f)
                )
                AnimalAttributeCard(
                    title = "Age",
                    data = animal.age?.toString(),
                    unit = " ans",
                    modifier = Modifier.weight(1f)
                )
                AnimalAttributeCard(
                    title = "Poids",
                    data = animal.weight?.toString(),
                    unit = " Kg",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        HorizontalDivider(Modifier.padding(vertical = 10.dp))
        if (model.activityConfigurationSelected.value == null) {
            if(activityList.isNotEmpty()){
                Text(
                    "Liste des activités",
                    color = MaterialTheme.colorScheme.onSurface
                )
                LazyColumn(
                    Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(activityList) {
                        ActivityItem(it, displayTime = true, model, navController)
                    }
                }
            }
            else{
                Column (
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Aucune activité pour l'instant...",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

        } else {
            ActivityConfigurationForm(
                model.activityConfigurationSelected.value!!,
                model,
            )
        }
    }
}
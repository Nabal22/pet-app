package com.mobile.animauxdomestiques.ui.screens.specie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobile.animauxdomestiques.MainViewModel
import com.mobile.animauxdomestiques.navigation.Screen
import com.mobile.animauxdomestiques.ui.components.ImagePicker

@Composable
fun SpecieScreen(model: MainViewModel, modifier: Modifier, navController: NavHostController, snackbarHostState: SnackbarHostState) {
    val specie = model.specieSelected.value
    val animals by model.getAllAnimalsFlowBySpecieId(specie?.specieId).collectAsState(listOf())

    if (specie == null) {
        navController.popBackStack()
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
            .padding(15.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row (
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.Start,
        )
        {
            Column(
                modifier = Modifier
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = specie.name,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                )
            }
        }
        HorizontalDivider(thickness = 3.dp)
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            repeat(animals.size) { idx ->
                val animal = animals[idx]
                ListItem(
                    leadingContent = {
                        ImagePicker(
                            animal.imagePath,
                            Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    },
                    headlineContent = { Text(animal.name) },
                    supportingContent = {
                        if (animal.breed?.isNotEmpty() == true) {
                            Text(
                                animal.breed
                            )
                        }
                    },
                    trailingContent = {
                        animal.gender?.let {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Icon(
                                    it.toIcon(),
                                    animal.gender.toString()
                                )

                            }
                        }

                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    modifier =
                    Modifier.clickable {
                        model.setAnimalSelected(animal)
                        navController.navigate(Screen.AnimalScreen.route)
                    }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
                if (idx != animals.size-1) HorizontalDivider()
            }
            if(animals.isEmpty()) {
                Text(
                    modifier = Modifier.fillMaxSize().padding(15.dp),
                    text= "Aucun " + specie.name + " n'est enregistré dans votre application."
                )
            }
        }
    }

}
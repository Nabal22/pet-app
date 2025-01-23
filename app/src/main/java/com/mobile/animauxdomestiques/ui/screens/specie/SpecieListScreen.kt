package com.mobile.animauxdomestiques.ui.screens.specie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobile.animauxdomestiques.MainViewModel
import com.mobile.animauxdomestiques.navigation.Screen

@Composable
fun SpecieListScreen(model: MainViewModel, navController: NavHostController, snackbarHostState: SnackbarHostState) {
    val species by model.getAllSpecies().collectAsState(listOf())

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
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp)
            )
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        repeat(species.size) { idx ->
            val specie = species[idx]
            ListItem(
                headlineContent = { Text(specie.name) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier =
                Modifier.clickable {
                    model.setSpecieSelected(specie)
                    navController.navigate(Screen.SpecieScreen.route)
                }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
            if (idx != species.size-1) HorizontalDivider()
        }
    }
}

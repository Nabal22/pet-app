package com.mobile.animauxdomestiques.ui.screens.animal

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.mobile.animauxdomestiques.MainViewModel
import com.mobile.animauxdomestiques.ui.components.animal.AnimalCardGrid

@Composable
fun AnimalListScreen(model: MainViewModel, navController: NavHostController, snackbarHostState: SnackbarHostState) {
    val animals by model.allAnimalsFlow.collectAsState(listOf())

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
    AnimalCardGrid(animals, model, navController)

}
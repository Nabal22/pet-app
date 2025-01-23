package com.mobile.animauxdomestiques.ui.components.dialog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mobile.animauxdomestiques.MainViewModel
import com.mobile.animauxdomestiques.navigation.Screen
import com.mobile.animauxdomestiques.ui.forms.ActivityForm
import com.mobile.animauxdomestiques.ui.forms.AnimalForm
import com.mobile.animauxdomestiques.ui.forms.SpecieForm

@Composable
fun DialogManager(model: MainViewModel, navHostController: NavHostController) {
    val context = LocalContext.current
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var showFullScreenDialog by model.showFullScreenDialog
    var showDeleteDialog by model.showDeleteDialog

    if (currentRoute != null) {
        DeleteDialog(
            showDialog = showDeleteDialog,
            onDismiss = { showDeleteDialog = false },
            title = when (currentRoute) {
                Screen.AnimalScreen.route -> "Suppression de l'animal"
                Screen.ActivityScreen.route -> "Suppression de l'activité"
                Screen.SpecieScreen.route -> "Suppression de l'espèce"
                else -> {""}
            },
            text = when (currentRoute) {
                Screen.AnimalScreen.route -> "Cela entrainera la supression de toutes ses activités programmées"
                Screen.ActivityScreen.route -> "Cela entrainera la supression de l'ensemble des activités configurés " +
                        "liés à celle que vous supprimez"
                Screen.SpecieScreen.route -> "Cela entrainera la supression de l'ensemble des animaux de cette espèce et " +
                        "des activités liés à cette espèce"
                else -> {""}
            },
            onConfirm = {
                when (currentRoute) {
                    Screen.AnimalScreen.route -> {
                        model.deleteAnimalSelected()
                    }
                    Screen.ActivityScreen.route -> {
                        model.deleteActivitySelected()
                    }
                    Screen.SpecieScreen.route -> {
                        model.deleteSpecieSelected()
                    }
                }
                showDeleteDialog = false
                navHostController.popBackStack()
            }
        )
        FullScreenDialog(
            showDialog = showFullScreenDialog,
            onDismiss = {
                when (currentRoute) {
                    Screen.AnimalListScreen.route -> {
                        model.clearAnimalInput()
                    }
                    Screen.ActivityListScreen.route -> {
                        model.clearActivityInput()
                    }
                    Screen.SpecieListScreen.route -> {
                        model.clearSpecieInput()
                    }
                }
                showFullScreenDialog = false
            },
            title = when (currentRoute) {
                Screen.AnimalListScreen.route -> "Ajouter un animal"
                Screen.AnimalScreen.route -> "Modifier l'animal"
                Screen.ActivityListScreen.route -> "Ajouter une activité"
                Screen.ActivityScreen.route -> "Modifier l'activité"
                Screen.SpecieListScreen.route -> "Ajouter une espèce"
                Screen.SpecieScreen.route -> "Modifier l'espèce"
                else -> {""}
            },
            onValidate = {
                when (currentRoute) {
                    Screen.AnimalListScreen.route -> {
                        model.addAnimal(context)
                    }
                    Screen.AnimalScreen.route -> {
                        model.updateAnimal(context)
                    }
                    Screen.ActivityListScreen.route -> {
                        model.addActivity()
                    }
                    Screen.ActivityScreen.route -> {
                        model.updateCurrentActivity()
                    }
                    Screen.SpecieListScreen.route -> {
                        model.addSpecie()
                    }
                    Screen.SpecieScreen.route -> {
                        model.updateCurrentSpecie()
                    }
                }
                if (!model.inputErrorDetected.value) {
                    showFullScreenDialog = false
                }
            },
            content = {
                when (currentRoute) {
                    Screen.AnimalListScreen.route, Screen.AnimalScreen.route -> AnimalForm(model)
                    Screen.ActivityListScreen.route, Screen.ActivityScreen.route -> ActivityForm(model)
                    Screen.SpecieListScreen.route, Screen.SpecieScreen.route -> SpecieForm(model)
                    else -> Text("Content not found")
                }
            }
        )
    }
}


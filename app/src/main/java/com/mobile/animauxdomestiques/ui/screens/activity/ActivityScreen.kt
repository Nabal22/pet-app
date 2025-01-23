package com.mobile.animauxdomestiques.ui.screens.activity

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
import com.mobile.animauxdomestiques.model.enums.ReminderType
import com.mobile.animauxdomestiques.navigation.Screen
import com.mobile.animauxdomestiques.ui.components.ImagePicker
import com.mobile.animauxdomestiques.utils.formatTime

@Composable
fun ActivityScreen(model: MainViewModel, modifier: Modifier, navController: NavHostController, snackbarHostState: SnackbarHostState) {
    val activity = model.activitySelected.value
    val activityConfigurationList by model.getActivityConfigurationFlow(activity?.activityId).collectAsState(listOf())

    if (activity == null) {
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
                    text = activity.name,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                )
                activity.description?.let {
                    Text(
                        text = it,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                }
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
            repeat(activityConfigurationList.size) { idx ->
                val activityConfiguration = activityConfigurationList[idx].activityConfiguration
                val animal = activityConfigurationList[idx].animal
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
                        if (activityConfiguration.isConfigured) {
                            Text(
                                formatTime(
                                    time = activityConfiguration.time ?: 0L,
                                    reminderType = activityConfiguration.reminderType
                                        ?: ReminderType.DAILY,
                                )
                            )
                        }
                        else {
                            Text("Pas encore programmé")
                        }
                    },
                    trailingContent = {
                        if (activityConfiguration.isConfigured){
                            Column (
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                activityConfiguration.reminderType?.toIcon()?.let {
                                    Icon(
                                        it,
                                        activityConfiguration.reminderType.toString()
                                    )
                                }
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
                if (idx != activityConfigurationList.size-1) HorizontalDivider()
            }
            if(activityConfigurationList.isEmpty()) {
                Text(
                    modifier = Modifier.fillMaxSize().padding(15.dp),
                    text= "Vous n'avez pas configuré cette activité à un animal..."
                )
            }
        }
    }

}
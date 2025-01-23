package com.mobile.animauxdomestiques.ui.components.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobile.animauxdomestiques.MainViewModel
import com.mobile.animauxdomestiques.model.ActivityConfigurationModel
import com.mobile.animauxdomestiques.model.enums.ReminderType
import com.mobile.animauxdomestiques.navigation.Screen
import com.mobile.animauxdomestiques.ui.theme.components.customCardColor
import com.mobile.animauxdomestiques.utils.formatTime

@Composable
fun ActivityItem(activityConfigurationModel: ActivityConfigurationModel, displayTime : Boolean? = false, model: MainViewModel, navController: NavHostController) {
    Card(
        colors = customCardColor(activityConfigurationModel.isConfigured),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            model.setActivitySelected(activityConfigurationModel.toActivity())
            navController.navigate(Screen.ActivityScreen.route)
        },
        modifier = Modifier
            .height(height = 130.dp)
            .fillMaxWidth()
    ) {
        Box(Modifier
            .fillMaxSize()
            .padding(10.dp)
        ) {
            Column(Modifier
                .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row (
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        activityConfigurationModel.name,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                    IconButton(
                        onClick = {
                            model.setActivityConfigurationFormSelected(activityConfigurationModel)
                        },
                        colors = IconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            disabledContentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Modification"
                        )
                    }
                }
                Text(
                    text = activityConfigurationModel.description.toString(),
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
                Row (
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    if (displayTime == true) {
                        if (activityConfigurationModel.isConfigured) {
                            Text(
                                activityConfigurationModel.reminderType.toString(),
                                fontSize = MaterialTheme.typography.bodySmall.fontSize
                            )
                            Text(
                                formatTime(
                                    time = activityConfigurationModel.time ?: 0L,
                                    reminderType = activityConfigurationModel.reminderType ?: ReminderType.DAILY,
                                ),
                                fontSize = MaterialTheme.typography.bodySmall.fontSize
                            )
                        }
                        else {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text =  "A configurer",
                                textAlign = TextAlign.End,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize
                            )
                        }
                    }
                }
            }
        }
    }

}

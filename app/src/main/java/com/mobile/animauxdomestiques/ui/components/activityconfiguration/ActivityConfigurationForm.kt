package com.mobile.animauxdomestiques.ui.components.activityconfiguration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mobile.animauxdomestiques.MainViewModel
import com.mobile.animauxdomestiques.model.ActivityConfigurationModel
import com.mobile.animauxdomestiques.model.enums.ReminderType
import com.mobile.animauxdomestiques.ui.components.CustomDateTimePicker
import com.mobile.animauxdomestiques.ui.components.CustomDropdownMenu
import com.mobile.animauxdomestiques.ui.theme.components.customCardColor
import com.mobile.animauxdomestiques.utils.longToLocalDateTime
import com.mobile.animauxdomestiques.utils.scheduleActivity

@Composable
fun ActivityConfigurationForm(
    activityConfiguration: ActivityConfigurationModel,
    model: MainViewModel
) {
    val context = LocalContext.current

    var selectedValue by remember {
        mutableStateOf(
            model.activityConfigurationSelected.value?.reminderType ?: ReminderType.DAILY
        )
    }

    val selectedDate = remember {
        mutableStateOf(longToLocalDateTime(model.activityConfigurationSelected.value?.time))
    }

    Card(
        colors = customCardColor(activityConfiguration.isConfigured),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
        )
        {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            activityConfiguration.name,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                        activityConfiguration.description?.let {
                            Text(
                                it,
                                fontSize = MaterialTheme.typography.titleSmall.fontSize
                            )
                        }
                    }
                    IconButton(onClick = {
                        model.setActivityConfigurationFormSelected(null)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back to activity"
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomDropdownMenu(
                        label = "Selectionnez une périodicité",
                        selectedItem = selectedValue,
                        items = ReminderType.entries,
                        onItemSelected = { selectedValue = it; },
                        itemToString = { it.toString() }
                    )

                    CustomDateTimePicker(
                        selectedValue,
                        selectedDate.value,
                    ) { selectedDate.value = it }

                    Button(onClick = {
                        model.updateActivityConfiguration(
                            activityConfiguration.activityConfigurationId,
                            selectedValue,
                            selectedDate.value
                        )
                        scheduleActivity(
                            context,
                            activityConfiguration,
                            selectedDate.value,
                            selectedValue
                        )
                        model.setActivityConfigurationFormSelected(null)
                    }) { Text("Valider") }

                }
            }
        }
    }

}
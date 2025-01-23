package com.mobile.animauxdomestiques.ui.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.TypeSpecimen
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.mobile.animauxdomestiques.MainViewModel
import com.mobile.animauxdomestiques.model.enums.ActivityLink

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActivityForm(model: MainViewModel) {
    val selectedSpecies = remember { model.selectedSpecies }
    val selectedAnimals = remember { model.selectedAnimals }

    val selectedOptions = remember { model.selectedOptions }

    val options = ActivityLink.entries

    val species by model.getAllSpecies().collectAsState(listOf())
    val animals by model.allAnimalsFlow.collectAsState(listOf())

    val filteredAnimals = animals.filter { animal ->
        selectedSpecies.none { it.specieId == animal.specieId }
    }

    LaunchedEffect(model.activitySelected.value) {
        selectedOptions[ActivityLink.DEFAULT.ordinal] = model.activitySelected.value?.let {
            model.isGlobalActivity(it.activityId)
        } == true
        model.activitySelected.value?.let { model.getSpeciesByActivityId(it.activityId) }
            ?.let { specieList ->
                model.updateSelectedSpecies(specieList)
                selectedOptions[ActivityLink.SPECIE.ordinal] = specieList.isNotEmpty()
            }
        model.activitySelected.value?.let {model.getAnimalsByActivityId(it.activityId)}
            ?.let { animalList ->
                model.updateSelectedAnimals(animalList)
                selectedOptions[ActivityLink.ANIMAL.ordinal] = selectedAnimals.isNotEmpty()
            }
    }

    if (model.inputErrorDetected.value) {
        AlertDialog(
            onDismissRequest = { model.inputErrorDetected.value = false },
            confirmButton = {
                Button(onClick = {
                    model.inputErrorDetected.value = false
                }) {
                    Text("Ok")
                }
            },
            title = { Text(text = "Certains champs sont incorrects") },
            text = { Text("Le nom de votre activité doit être renseigné") },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 15.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        OutlinedTextField(
            value = model.newActivityName.value,
            onValueChange = { model.onNewActivityNameChange(it) },
            label = { Text("Nom") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            minLines = 3,
            maxLines = 3,
            value = model.newActivityDescription.value,
            onValueChange = { model.onNewActivityDescriptionChange(it) },
            label = { Text("Description (Optionnel)") },
            modifier = Modifier.fillMaxWidth()
        )
        Column (
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "Présence de l'activité par :",
                Modifier.padding(5.dp)
            )
            MultiChoiceSegmentedButtonRow(
                Modifier.fillMaxWidth()
            ) {
                options.forEachIndexed { index, activityLink ->
                    SegmentedButton(
                        enabled = when (activityLink) {
                            ActivityLink.ANIMAL -> filteredAnimals.isNotEmpty()
                            else -> true
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        checked = when (activityLink) {
                            ActivityLink.ANIMAL -> filteredAnimals.isNotEmpty() && selectedOptions[index]
                            else -> selectedOptions[index]
                        },
                        onCheckedChange = {
                            when (activityLink) {
                                ActivityLink.DEFAULT -> {
                                    selectedSpecies.clear()
                                    selectedAnimals.clear()
                                    if (!selectedOptions[index]) {
                                        selectedOptions.fill(false)
                                        selectedOptions[index] = true
                                    } else {
                                        selectedOptions[index] = false
                                    }
                                }

                                ActivityLink.SPECIE -> {
                                    if (!selectedOptions[index]) {
                                        selectedOptions[ActivityLink.DEFAULT.ordinal] = false
                                        selectedOptions[index] = !selectedOptions[index]
                                    } else {
                                        selectedSpecies.clear()
                                        selectedOptions[index] = false
                                    }
                                }

                                ActivityLink.ANIMAL -> {
                                    if (!selectedOptions[index]) {
                                        selectedOptions[ActivityLink.DEFAULT.ordinal] = false
                                        selectedOptions[index] = !selectedOptions[index]
                                    } else {
                                        selectedAnimals.clear()
                                        selectedOptions[index] = false
                                    }
                                }
                            }
                        },
                        icon = { SegmentedButtonDefaults.Icon(
                            when (activityLink) {
                                ActivityLink.ANIMAL -> filteredAnimals.isNotEmpty() && selectedOptions[index]
                                else -> selectedOptions[index]
                            },
                        ) },
                        label = { Text(activityLink.toString()) },
                        colors = SegmentedButtonDefaults.colors(
                            disabledInactiveContainerColor = MaterialTheme.colorScheme.surfaceDim,
                            disabledInactiveContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
        if (selectedOptions[ActivityLink.SPECIE.ordinal]) {
            Column (
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        Icons.Default.TypeSpecimen,
                        "Espèces",
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Text(
                        text = "Espèces",
                        Modifier.padding(top = 10.dp, start = 5.dp)
                    )
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    species.forEach { specie ->
                        FilterChip(
                            selected = specie in selectedSpecies,
                            onClick = {
                                if (specie in selectedSpecies) {
                                    model.selectedSpecies.remove(specie)
                                } else {
                                    model.selectedSpecies.add(specie)
                                }
                            },
                            label = { Text(text = specie.name) },
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    }
                }
            }
        }
        if (selectedOptions[ActivityLink.ANIMAL.ordinal]) {
            Column (
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        Icons.Default.Pets,
                        "Animaux",
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Text(
                        text = "Animaux",
                        Modifier.padding(top = 10.dp, start = 5.dp)
                    )
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    filteredAnimals.forEach { animal ->
                        FilterChip(
                            selected = animal in selectedAnimals,
                            onClick = {
                                if (animal in selectedAnimals) {
                                    selectedAnimals.remove(animal)
                                } else {
                                    selectedAnimals.add(animal)
                                }
                            },
                            label = { Text(text = animal.name) },
                            modifier = Modifier.padding(end = 5.dp),
                        )
                    }
                }
            }
        }
    }
}

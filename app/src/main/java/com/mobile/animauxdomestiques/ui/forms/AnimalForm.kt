package com.mobile.animauxdomestiques.ui.forms

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.mobile.animauxdomestiques.MainViewModel
import com.mobile.animauxdomestiques.model.enums.Gender
import com.mobile.animauxdomestiques.ui.components.CustomDropdownMenu
import com.mobile.animauxdomestiques.ui.components.ImagePicker
import com.mobile.animauxdomestiques.ui.theme.components.customIconButtonColor

@Composable
fun AnimalForm(model: MainViewModel) {
    val species by model.getAllSpecies().collectAsState(listOf())

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            model.selectedImagePath.value = uri.toString()
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
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
            text = { Text("Le nom doit être non nul et vous devez sélectionner une espèce") },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp, vertical = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        item {
            OutlinedTextField(
                value = model.newAnimalName.value,
                onValueChange = { model.onNewAnimalNameChange(it) },
                label = { Text("Nom") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            CustomDropdownMenu(
                label = "Sélectionnez une espèce",
                selectedItem = species.firstOrNull { it.specieId == model.specieIdSelected.value },
                items = species,
                onItemSelected = { model.setSpecieIdSelected(it.specieId) },
                itemToString = { it.name },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = model.newAnimalBreed.value,
                onValueChange = { model.onNewAnimalBreedChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Race (Optionel)") },
            )
        }
        item {
            CustomDropdownMenu(
                label = "Sélectionnez un genre",
                selectedItem = model.newAnimalGender.value,
                items = Gender.entries,
                onItemSelected = { model.setNewAnimalGender(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Row(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = model.newAnimalAge.value?.toString() ?: "",
                    onValueChange = { input ->
                        val newAge = input.toIntOrNull()
                        model.onNewAnimalAgeChange(newAge)
                    },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text("Age (Optionel)") },
                )
                Spacer(modifier = Modifier.width(20.dp))
                OutlinedTextField(
                    value = model.newAnimalWeight.value?.toString() ?: "",
                    onValueChange = { input ->
                        val newWeight = input.toFloatOrNull()
                        model.onNewAnimalWeightChange(newWeight)
                    },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text("Poids en kg (Optionel)") },
                )
            }
        }

        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Choisissez une image pour votre animal")
                Spacer(modifier = Modifier.height(10.dp))
                ImagePicker(
                    model.selectedImagePath.value,
                    Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Button(onClick = {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }) {
                        Text("Sélectionner une image")
                    }
                    IconButton(
                        colors = customIconButtonColor(),
                        onClick = { model.selectedImagePath.value = "" }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Go back to activity"
                        )
                    }
                }
            }
        }
    }
}

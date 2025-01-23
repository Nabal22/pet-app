package com.mobile.animauxdomestiques.ui.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.mobile.animauxdomestiques.MainViewModel

@Composable
fun SpecieForm(model: MainViewModel) {

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
            text = { Text("Le nom de votre espèce doit être renseigné") },
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
            value = model.newSpecieName.value,
            onValueChange = { model.onNewSpecieNameChange(it) },
            label = { Text("Nom de l'espèce") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

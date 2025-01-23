package com.mobile.animauxdomestiques.ui.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

@Composable
fun DeleteDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String,
    text: String,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Annuler")
                }
            },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("Confirmer")
                }
            },
            title = { Text(text = title, fontSize = MaterialTheme.typography.headlineLarge.fontSize) },
            text = { Text(text) },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }
}

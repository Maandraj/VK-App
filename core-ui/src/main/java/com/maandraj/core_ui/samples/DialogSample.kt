package com.maandraj.core_ui.samples

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.maandraj.core_ui.dialogShape


@Composable
fun DialogSample(
    title: String,
    text: String,
    confirmButtonText: String,
    dismissButtonText: String,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            shape= dialogShape,
            onDismissRequest = {
            },
            title = {
                Text(title)
            },
            confirmButton = {
                Button(
                    onClick = {
                        setShowDialog(true)
                    },
                ) {
                    Text(confirmButtonText)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        setShowDialog(false)
                    },
                ) {
                    Text(dismissButtonText)
                }
            },
            text = {
                Text(text)
            },
        )
    }
}
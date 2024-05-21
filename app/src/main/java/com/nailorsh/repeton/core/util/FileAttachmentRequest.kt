package com.nailorsh.repeton.core.util

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun fileAttachmentRequest(
    onFileChosen: (Uri) -> Unit,
    onFileChoosingFailed: () -> Unit
): () -> Unit {
    val context = LocalContext.current

    val pickFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                onFileChosen(uri)
            } else {
                onFileChoosingFailed()
            }
        }
    )

    return {
        pickFileLauncher.launch("*/*")
    }
}
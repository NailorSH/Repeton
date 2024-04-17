package com.nailorsh.repeton.core.util

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun cameraRequest(
    onImageCaptured: (Bitmap) -> Unit,
    onPermissionDenied: () -> Unit
): () -> Unit {
    val context = LocalContext.current

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            // Обработка полученного изображения
            if (bitmap != null) {
                onImageCaptured(bitmap)
            }
        }
    )

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Если разрешение получено, запускаем камеру
                takePictureLauncher.launch(null)
            } else {
                // Если пользователь не предоставил разрешение, вызываем коллбэк
                onPermissionDenied()
            }
        }
    )

    // Функция для проверки разрешения и запуска камеры
    return {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                // Если разрешение уже есть, сразу запускаем камеру
                takePictureLauncher.launch(null)
            }
            else -> {
                // Запрашиваем разрешение, так как оно еще не предоставлено
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}

package com.nailorsh.repeton.core.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun cameraRequest(
    onImageCaptured: (Uri) -> Unit,
    onPermissionDenied: () -> Unit
): () -> Unit {
    val context = LocalContext.current

    var currentPhotoPath by remember { mutableStateOf("") }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success: Boolean ->
            if (success) {
                val photoUri = Uri.fromFile(File(currentPhotoPath))
                onImageCaptured(photoUri)
            }
        }
    )

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Если разрешение получено, запускаем камеру
                dispatchTakePictureIntent(
                    context,
                    takePictureLauncher,
                    currentPhotoPathSetter = { path ->
                        currentPhotoPath = path
                    })
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
                // Если разрешение уже есть, запускаем камеру
                dispatchTakePictureIntent(
                    context,
                    takePictureLauncher,
                    currentPhotoPathSetter = { path ->
                        currentPhotoPath = path
                    })
            }

            else -> {
                // Запрашиваем разрешение, так как оно еще не предоставлено
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Throws(IOException::class)
private fun createImageFile(context: Context, currentPhotoPathSetter: (String) -> Unit): File {
    // Создание имени файла изображения
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        // Сохранение пути для использования в ACTION_VIEW намерении
        currentPhotoPathSetter(absolutePath)
    }
}

private fun dispatchTakePictureIntent(
    context: Context,
    takePictureLauncher: ActivityResultLauncher<Uri>,
    currentPhotoPathSetter: (String) -> Unit
) {
    val photoFile: File? = try {
        createImageFile(context, currentPhotoPathSetter)
    } catch (ex: IOException) {
        // Ошибка при создании файла
        null
    }

    // Продолжаем только если файл был успешно создан
    photoFile?.also {
        val photoURI: Uri = FileProvider.getUriForFile(
            context,
            "com.nailorsh.repeton.fileprovider",
            it
        )
        takePictureLauncher.launch(photoURI)
    }
}
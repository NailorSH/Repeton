package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class FileDto(
    @PropertyName("url") val url: String = "",
    @PropertyName("fileName") val fileName: String = ""
)

data class ImageDto(
    @PropertyName("url") val url: String = "",
    @PropertyName("description") val description: String = ""
)

data class AttachmentDto(
    @Exclude val id: String = "",
    @PropertyName("image") val image: ImageDto? = null,
    @PropertyName("file") val file: FileDto? = null
)
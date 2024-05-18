package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class LessonDto(
    @Exclude var id: String = "",
    @PropertyName("tutorId") val tutorId: String = "",
    @PropertyName("studentIds") val studentIds: List<String> = emptyList(),
    @PropertyName("subjectId") val subjectId: String = "",
    @PropertyName("topic") val topic: String = "",
    @PropertyName("description") val description : String = "",
    @PropertyName("startTime") val startTime: Timestamp = Timestamp.now(),
    @PropertyName("endTime") val endTime: Timestamp = Timestamp.now(),
    @Exclude val homework: HomeworkDto? = null,
    @PropertyName("additionalMaterials") val additionalMaterials: String = ""
)

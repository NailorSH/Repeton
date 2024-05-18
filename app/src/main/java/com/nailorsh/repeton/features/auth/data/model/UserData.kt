package com.nailorsh.repeton.features.auth.data.model

data class UserData(
    val name: String = "",
    val surname: String = "",
    val phone: String = "",
    val vkIdToken: String? = null,
    val canBeTutor: Boolean = false
)

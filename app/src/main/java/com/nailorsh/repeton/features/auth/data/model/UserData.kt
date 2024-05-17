package com.nailorsh.repeton.features.auth.data.model

data class UserData(
    val name: String = "",
    val surname: String = "",
    val canBeTutor: Boolean = false,
    val phoneNumber : String = ""
)

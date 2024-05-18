package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class UserDto(
    @Exclude val id: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("surname") val surname: String = "",
    @PropertyName("canBeTutor") val canBeTutor: Boolean = false,
    @PropertyName("phoneNumber") val phoneNumber : String = ""
) {
    companion object {
        val Anonymous = UserDto(
            id = "-1",
            name = "Anonymous",
            surname = "",
            canBeTutor = false,
            phoneNumber = "No number provided"
        )
    }
}
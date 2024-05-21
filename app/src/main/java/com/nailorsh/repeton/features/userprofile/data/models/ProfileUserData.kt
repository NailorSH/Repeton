package com.nailorsh.repeton.features.userprofile.data.models

data class ProfileUserData(
    val name : String,
    val surname : String,
    val phoneNumber : String,
    val photoSrc : String? = null,
    val isTutor : Boolean,
)
package com.nailorsh.repeton.common.data.models

sealed interface User {
    val id: String
    val name: String
    val surname: String
    val middleName: String?
    val about: String?
    val photoSrc: String?
}
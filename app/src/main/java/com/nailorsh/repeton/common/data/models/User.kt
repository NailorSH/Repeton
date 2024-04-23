package com.nailorsh.repeton.common.data.models

sealed interface User {
    val id: Int
    val name: String
    val surname: String
    val middleName: String?
    val about: String?
    val photoSrc: String?
}
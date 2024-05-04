package com.nailorsh.repeton.common.data.models.user

sealed interface User {
    val id: UserId
    val name: String
    val surname: String
    val middleName: String?
    val about: String?
    val photoSrc: String?
}

@JvmInline
value class UserId(val value: String)

fun String.toUserId(): UserId = UserId(this)
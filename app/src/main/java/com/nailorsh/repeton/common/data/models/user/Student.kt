package com.nailorsh.repeton.common.data.models.user

data class Student(
    override val id: UserId,
    override val name: String,
    override val surname: String,
    override val middleName: String?,
    override val about: String?,
    override val photoSrc: String?
) : User
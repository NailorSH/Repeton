package com.nailorsh.repeton.common.data.models

data class Student(
    override val id: Int,
    override val name: String,
    override val surname: String,
    override val middleName: String?,
    override val about: String?,
    override val photoSrc: String?
) : User

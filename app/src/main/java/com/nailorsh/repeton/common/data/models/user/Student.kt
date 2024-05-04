package com.nailorsh.repeton.common.data.models.user

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.location.Location

data class Student(
    override val id: Id,
    override val name: String,
    override val surname: String,
    override val middleName: String?,
    override val about: String?,
    override val photoSrc: String?,
    override val location: Location?
) : User
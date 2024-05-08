package com.nailorsh.repeton.common.data.models.user

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.location.Location

data class Student(
    override val id: Id,
    override var name: String,
    override var surname: String,
    override var middleName: String?,
    override var about: String?,
    override var photoSrc: String?,
    override var location: Location?
) : User
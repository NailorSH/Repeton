package com.nailorsh.repeton.common.data.models.user

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.location.Location

interface User {
    val id: Id
    val name: String
    val surname: String
    val middleName: String?
    val about: String?
    val photoSrc: String?
    val phoneNumber: String
    val location: Location?
    val isTutor : Boolean
}


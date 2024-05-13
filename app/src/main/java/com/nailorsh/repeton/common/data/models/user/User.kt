package com.nailorsh.repeton.common.data.models.user

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.location.Location

sealed interface User {
    val id: Id
    var name: String
    var surname: String
    var middleName: String?
    var about: String?
    var photoSrc: String?
    var location: Location?
}


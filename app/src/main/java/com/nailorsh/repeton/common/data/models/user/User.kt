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

    companion object {
        val None = object : User {
            override val id: Id = Id("0")
            override val name: String = "User"
            override val surname: String = "User"
            override val middleName: String? = null
            override val about: String? = null
            override val photoSrc: String? = null
            override val phoneNumber: String = "+11111111111"
            override val location: Location? = null
            override val isTutor: Boolean = false

        }
    }

}


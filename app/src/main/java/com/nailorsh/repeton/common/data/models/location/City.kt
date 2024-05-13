package com.nailorsh.repeton.common.data.models.location

import com.google.firebase.firestore.DocumentSnapshot
import com.nailorsh.repeton.common.data.models.Id

data class City(
    val id: Id,
    val name: Map<String, String>? = null,
)

fun DocumentSnapshot.toCityWithId(): City {
    val id = Id(id)
    val name = (get("name") as? Map<*, *>)?.mapNotNull { (key, value) ->
        (key as? String)?.let { k ->
            (value as? String)?.let { v -> k to v }
        }
    }?.toMap()

    return City(
        id = id,
        name = name
    )
}
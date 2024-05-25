package com.nailorsh.repeton.common.data.models.language

import com.nailorsh.repeton.common.data.models.Id

data class Language(
    val id: Id,
    val name: String,
    val level: LanguageLevel,
)

enum class LanguageLevel(val value: String) {
    A1("A1"),
    A2("A2"),
    B1("B1"),
    B2("B2"),
    C1("C1"),
    C2("C2"),
    NATIVE("Native"),
    OTHER("Other");

    override fun toString(): String {
        return value
    }
}

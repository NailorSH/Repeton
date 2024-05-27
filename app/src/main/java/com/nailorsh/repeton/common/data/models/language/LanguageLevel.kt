package com.nailorsh.repeton.common.data.models.language

import com.nailorsh.repeton.common.data.models.Id

enum class LanguageLevel(
    val id: Id,
    val value: String
) {
    A1(Id("jjqCWt0RFanSaGg12yeU"), "A1"),
    A2(Id("Lzd1DLM6mztpIBhP2WSw"), "A2"),
    B1(Id("bOYuy63ahFFoYB9plUAS"), "B1"),
    B2(Id("9ZL3fsyl6q0peWAufeU2"), "B2"),
    C1(Id("KGx88VZzqNxWAkFVzzTX"), "C1"),
    C2(Id("XtwjKkqSLRVbZIfwkVJl"), "C2"),
    NATIVE(Id("TOZi95ok6w65aHLwhZwS"), "Родной"),
    OTHER(Id("fK0dM8bdw3zE35lKmT7B"), "Другой");

    override fun toString(): String {
        return value
    }

    companion object {
        fun getLevelByString(level: String): LanguageLevel {
            return values().find { it.value == level } ?: OTHER
        }

        fun fromId(id: Id): LanguageLevel {
            return LanguageLevel.values().find { it.id == id } ?: OTHER
        }
    }
}
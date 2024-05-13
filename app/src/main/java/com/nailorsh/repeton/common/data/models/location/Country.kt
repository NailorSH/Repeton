package com.nailorsh.repeton.common.data.models.location

import com.google.firebase.firestore.DocumentSnapshot
import com.nailorsh.repeton.common.data.models.Id

data class Country(
    val id: Id,
    val code: String,
    val name: Map<String, String>,
) {
    fun getFlagEmoji(): String {
        val asciiOffset = 'A'.code // ASCII code for 'A'
        val emojiRegionBase = 0x1F1E6 // Base Unicode for regional indicator symbols
        return if (code.length >= 2) {
            val firstCharEmojiCode =
                Character.codePointAt(code, 0) - asciiOffset + emojiRegionBase
            val secondCharEmojiCode =
                Character.codePointAt(code, 1) - asciiOffset + emojiRegionBase

            String(Character.toChars(firstCharEmojiCode)) + String(
                Character.toChars(
                    secondCharEmojiCode
                )
            )
        } else {
            // Return the default flag emoji if countryCode is null or not valid
            "🇩🇩"
        }
    }
}

fun DocumentSnapshot.toCountryWithId(): Country {
    val id = Id(id)
    val code = getString("code") ?: throw NoSuchElementException("Country code field not found")
    val name = (get("name") as? Map<*, *>)?.mapNotNull { (key, value) ->
        (key as? String)?.let { k ->
            (value as? String)?.let { v -> k to v }
        }
    }?.toMap() ?: throw NoSuchElementException("Country name field not found")

    return Country(
        id = id,
        code = code,
        name = name
    )
}
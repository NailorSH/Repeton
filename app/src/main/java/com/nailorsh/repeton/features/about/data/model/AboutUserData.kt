package com.nailorsh.repeton.features.about.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.contact.ContactType
import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.language.LanguageWithLevel


sealed class AboutContactItem(
    @StringRes val name : Int,
    @StringRes val placeholder : Int,
    @DrawableRes val icon : Int,
    val type : ContactType,
    val value : String
) {

    data class Telegram(val telegramValue : String = "") : AboutContactItem(
        name = R.string.telegram,
        icon = R.drawable.ic_telegram,
        type = ContactType.TELEGRAM,
        placeholder = R.string.telegram_placeholder,
        value = telegramValue
    )

    data class WhatsApp(val whatsAppValue : String = "") : AboutContactItem(
        name = R.string.whatsapp,
        icon = R.drawable.ic_whatsapp,
        type = ContactType.WHATSAPP,
        placeholder = R.string.whatsapp_placeholder,
        value = whatsAppValue
    )

    data class Other(val otherValue : String = "") : AboutContactItem(
        name = R.string.other,
        icon = R.drawable.ic_info,
        type = ContactType.OTHER,
        placeholder = R.string.other_placeholder,
        value = otherValue
    )
}

data class AboutUserData(
    val name : String,
    val surname : String,

    val photoSrc : String ?= null,
    val isTutor : Boolean,

    val about : String? = null,
    val education: Education? = null,
    val languagesWithLevels: List<LanguageWithLevel>? = null,

    val contacts : List<AboutContactItem>,
)

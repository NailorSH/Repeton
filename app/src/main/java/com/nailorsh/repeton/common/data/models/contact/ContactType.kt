package com.nailorsh.repeton.common.data.models.contact

enum class ContactType(val value: String) {
    WHATSAPP("WhatsApp"),
    TELEGRAM("Telegram"),
    OTHER("Другое");

    override fun toString(): String {
        return value
    }

    companion object {
        fun getTypeByString(value: String): ContactType {
            return ContactType.values().find { it.value == value } ?: OTHER
        }
    }
}

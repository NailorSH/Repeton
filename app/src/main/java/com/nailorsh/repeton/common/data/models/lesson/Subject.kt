package com.nailorsh.repeton.common.data.models.lesson

import com.nailorsh.repeton.common.data.models.Id

data class Subject(
    val id: Id,
    val name: Map<String, String>
) {
    companion object {
        val None = Subject(Id("-1"), mapOf("ru" to ""))
    }
}

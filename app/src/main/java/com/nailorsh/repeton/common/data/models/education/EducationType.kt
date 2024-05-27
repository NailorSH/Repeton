package com.nailorsh.repeton.common.data.models.education

import com.nailorsh.repeton.common.data.models.Id

enum class EducationType(
    val id: Id,
    val value: String
) {
    SECONDARY_GENERAL(Id("Nd6KEglwbh3GbCK2GrAV"), "Среднее общее"),
    SECONDARY_PROFESSIONAL(Id("ZErPHIJZVaGAD0afW8FK"), "Среднее профессиональное"),
    SPECIALIST(Id("ZvAOij8UM1a3IKXi0SSB"), "Специалитет"),
    BACHELOR(Id("cCv3zt72RPvcakRG60rl"), "Бакалавриат"),
    MASTER(Id("urg5mS110zk3rVpmQuoL"), "Магистратура"),
    POSTGRADUATE(Id("mGnNoJzXepMlKUAVafCc"), "Аспирантура"),
    OTHER(Id("mVQKjgwSHytSRY6TI0PE"), "Другое");

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromId(id: Id): EducationType {
            return values().find { it.id == id } ?: OTHER
        }
    }
}

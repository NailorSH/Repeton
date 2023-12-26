package com.nailorsh.repeton.data

interface AppContainer {
    val repetonRepository: RepetonRepository
}

class DefaultAppContainer : AppContainer {

    override val repetonRepository: RepetonRepository by lazy {
        FakeRepetonRepository()
    }
}
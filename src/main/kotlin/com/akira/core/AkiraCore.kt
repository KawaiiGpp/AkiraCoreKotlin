package com.akira.core

import com.akira.core.api.AkiraPlugin

class AkiraCore : AkiraPlugin() {
    companion object {
        lateinit var instance: AkiraCore
            private set
    }

    init {
        instance = this
    }
}
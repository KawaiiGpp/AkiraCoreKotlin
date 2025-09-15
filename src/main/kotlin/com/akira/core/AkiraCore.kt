package com.akira.core

import com.akira.core.api.AkiraPlugin
import com.akira.core.command.InfoCommand

class AkiraCore : AkiraPlugin() {
    companion object {
        lateinit var instance: AkiraCore
            private set
    }

    init {
        instance = this
    }

    override fun onEnable() {
        super.onEnable()

        setupCommand(InfoCommand(this))
    }
}
package com.istarvin

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class SendToServer: Plugin() {
    override fun load(context: Context) {
        registerVideoClickAction(SendToServerAction())
    }
}
package com.istarvin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class SendToServerPlugin: Plugin() {
    override fun load(context: Context) {
        val sharedPref = context.getSharedPreferences("SendToServer", Context.MODE_PRIVATE)

        val url = sharedPref?.getString("url", null) ?: ""

        registerVideoClickAction(SendToServerAction(url))

        val activity = context as AppCompatActivity
        openSettings = {
            val frag = SettingsFragment(this, sharedPref)
            frag.show(activity.supportFragmentManager, "SettingsFragment")
        }
    }
}
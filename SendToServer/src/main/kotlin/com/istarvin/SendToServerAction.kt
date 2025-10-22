package com.istarvin

import android.content.Context
import androidx.core.R
import com.lagradost.cloudstream3.actions.VideoClickAction
import com.lagradost.cloudstream3.ui.result.LinkLoadingResult
import com.lagradost.cloudstream3.ui.result.ResultEpisode
import com.lagradost.cloudstream3.utils.txt

class SendToServerAction(
    val serverUrl: String
): VideoClickAction() {
    override val name = txt("Send to server")

    override val oneSource = true

    override fun shouldShow(context: Context?, video: ResultEpisode?) = true

    override suspend fun runAction(
        context: Context?,
        video: ResultEpisode,
        result: LinkLoadingResult,
        index: Int?
    ) {
        println(serverUrl)
        println(result.links[index ?: 0])
        println(video)
    }
}
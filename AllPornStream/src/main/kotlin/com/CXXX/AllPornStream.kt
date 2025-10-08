package com.CXXX

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lagradost.api.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class AllPornStream : MainAPI() {
    override var mainUrl = "https://allpornstream.com"
    override var name = "AllPornStream"
    override val hasMainPage = true
    override val hasDownloadSupport = true
    override val vpnStatus = VPNStatus.MightBeNeeded
    override val supportedTypes = setOf(TvType.NSFW)

    override val mainPage = mainPageOf(
        "studio=ElegantAngel" to "Elegant Angel",
        "studio=Blacked" to "Blacked",
        "studio=DadCrush" to "Dad Crush",
        "studio=Shoplyfter" to "Shoplyfter",
        "studio=Tushy" to "Tushy",
        "studio=SisLovesMe" to "SisLovesMe",
        "studio=OnlyTarts" to "OnlyTarts",
        "studio=TouchMyWife" to "TouchMyWife",
        "studio=FamilyTherapyXXX" to "FamilyTherapy",
        "studio=PornFidelity" to "PornFidelity",
        "studio=TeenFidelity" to "TeenFidelity",
        "studio=MyLifeInMiami" to "MyLifeInMiami",
        "studio=GangbangCreampie" to "GangbangCreampie",
        "studio=FreeuseFantasy" to "FreeuseFantasy",
        "studio=ExxxtraSmall" to "ExxxtraSmall",
        "studio=SpankMonster" to "SpankMonster",
        "studio=RealityJunkies" to "RealityJunkies",
        "studio=RKPrime" to "RKPrime",
        "studio=FreeuseMILF" to "FreeuseMILF",
        "studio=MomComesFirst" to "MomComesFirst",
        "studio=MySistersHotFriend" to "MySistersHotFriend",
        "studio=MommyBlowsBest" to "MommyBlowsBest",
        "studio=DaughterSwap" to "DaughterSwap",
        "studio=PornForce" to "PornForce",
        "studio=Slayed" to "Slayed",
        "studio=MomSwap" to "Mom Swap",
        "studio=BangRealTeens" to "BangRealTeens",
        "studio=TeenyTaboo" to "TeenyTaboo",
        "studio=MyBabysittersClub" to "MyBabysittersClub",
        "studio=Hunt4K" to "Hunt4K",
        "studio=PropertySex" to "PropertySex",
        "studio=FamilyHookups" to "FamilyHookups",
        "studio=MomIsHorny" to "MomIsHorny",
        "studio=Hustler" to "Hustler",
        "studio=SexMex" to "SexMex Studio",
        "studio=BrazzersExxtra" to "BrazzersExxtra Studio",
        "studio=EvilAngel" to "EvilAngel Studio",
        "studio=PornWorld" to "PornWorld Studio",
        "studio=DorcelClub" to "DorcelClub Studio",
        "studio=TabooHeat" to "TabooHeat Studio",
        "studio=MyPervyFamily" to "MyPervyFamily Studio",
        "studio=BangBus" to "BangBus Studio",
        "studio=PureTaboo" to "PureTaboo Studio",
        "studio=PervMom" to "PervMom Studio",
        "studio=NubileFilms" to "NubileFilms Studio",
        "studio=FamilyStrokes" to "FamilyStrokes Studio",
        "studio=BrattySis" to "BrattySis Studio",
        "studio=MyFriendsHotMom" to "MyFriendsHotMom Studio",
        "studio=SweetSinner" to "SweetSinner Studio",
        "studio=FamilyXXX" to "FamilyXXX Studio",
        "studio=StepSiblingsCaught" to "StepSiblingsCaught Studio",
        "studio=japan-hdv" to "Japan HDV",
        "studio=erito" to "Erito",
        "studio=public-agent" to "Public Agent",
        "studio=cum-4-k" to "Cum (4K)",
    )

    override suspend fun getMainPage(
        page: Int,
        request: MainPageRequest
    ): HomePageResponse {
        val res = app.get("$mainUrl/api/posts?${request.data}&page=$page").parsedSafe<Posts>()?.posts
        val home = res?.map {
            it.toSearchResult()
        }
        return newHomePageResponse(
            list = HomePageList(
                name = request.name,
                list = home!!,
                isHorizontalImages = true
            ),
            hasNext = true
        )
    }

    private fun PostMain.toSearchResult(): SearchResponse {
        val title = this.videoTitle
        val href = this.id
        val posterUrl = this.imageDetails
            .firstOrNull { it.startsWith("http") }
        return newMovieSearchResponse(title, href, TvType.NSFW) {
            this.posterUrl = posterUrl
        }
    }

    private fun RelatedPost.toSearchResult(): SearchResponse {
        val title = this.videoTitle
        val href = this.id
        val posterUrl = this.imageDetails
            .firstOrNull { it.startsWith("http") }
        return newMovieSearchResponse(title, href, TvType.NSFW) {
            this.posterUrl = posterUrl
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val searchResponse = mutableListOf<SearchResponse>()
            val res = app.get("$mainUrl/api/posts?search=$query&page=$i").parsedSafe<Posts>()?.posts.orEmpty()
            val results = res.map { it.toSearchResult() }
            val newResults = results.filterNot { it in searchResponse }
            searchResponse.addAll(newResults)
            if (newResults.isEmpty()) break
        }

        return searchResponse
    }

    override suspend fun load(url: String): LoadResponse {
        val res = app.get("$mainUrl/api/post?id=${url.substringAfterLast("/")}").parsedSafe<Load>()
        val loaddata = res?.post
        val title = loaddata?.videoTitle ?: "Unknown"
        val poster = loaddata?.imageDetails
            ?.firstOrNull { it.startsWith("http") }
        val tags = loaddata?.categories?.map { it }
        val description = loaddata?.videoDescription ?: ""
        val recommendations= res?.relatedPosts?.map { it.toSearchResult() }
        val hrefs = res?.urls?.map { it.url }?.toJson()
        return newMovieLoadResponse(title, url, TvType.NSFW, hrefs) {
            this.posterUrl = poster
            this.plot = description
            this.tags = tags
            this.recommendations = recommendations
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean = coroutineScope {
        val parsedList = data.fromJson<List<String>>()
        parsedList.map { url ->
            launch {
                Log.d("Phisher", url)
                loadExtractor(url, "$mainUrl/", subtitleCallback, callback)
            }
        }.joinAll()

        true
    }

    val gson = Gson()
    private inline fun <reified T> String.fromJson(): T = gson.fromJson(this, object : TypeToken<T>() {}.type)
}

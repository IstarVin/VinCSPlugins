import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("org.jetbrains.kotlin.android")
}
version = 4

android {
    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        android.buildFeatures.buildConfig=true
    }
}
dependencies {
    implementation("com.google.android.material:material:1.12.0")
    val cloudstream by configurations
    cloudstream("com.lagradost:cloudstream3:pre-release")
}

cloudstream {
    language = "en"
    description = "Sends url to server"
    authors = listOf("IstarVin")
    status = 1
    tvTypes = listOf(
        "Movie",
//        "TvSeries",
//        "AsianDrama",
//        "Anime",
    )

    iconUrl = "https://github.com/SaurabhKaperwan/CSX/raw/refs/heads/master/CineStream/icon.jpg"

    requiresResources = true
}


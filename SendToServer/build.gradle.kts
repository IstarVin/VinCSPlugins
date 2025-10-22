import org.jetbrains.kotlin.konan.properties.Properties

version = 1

android {
    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        android.buildFeatures.buildConfig=true
    }
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
}

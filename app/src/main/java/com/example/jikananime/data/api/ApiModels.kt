package com.example.jikananime.data.api

import com.google.gson.annotations.SerializedName

data class TopAnimeResponse(
    val data: List<AnimeSummary>
)

data class AnimeSummary(
    @SerializedName("mal_id") val malId: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val synopsis: String?,
    @SerializedName("images") val images: Images,
    val trailer: Trailer?
)

// Anime details response
data class AnimeDetailsResponse(
    val data: AnimeDetails
)

data class AnimeDetails(
    @SerializedName("mal_id") val malId: Int,
    val title: String,
    val synopsis: String?,
    val episodes: Int?,
    val score: Double?,
    @SerializedName("images") val images: Images,
    val trailer: Trailer?,
    val genres: List<Genre>,
    @SerializedName("studios") val studios: List<Studio>,
    @SerializedName("producers") val producers: List<Producer>,
)

data class Images(
    val jpg: ImageUrls
)

data class ImageUrls(
    @SerializedName("image_url") val imageUrl: String?
)

data class Trailer(
    @SerializedName("url") val url: String?
)

data class Genre(
    val name: String
)

data class Studio(
    val name: String
)

data class Producer(
    val name: String
)

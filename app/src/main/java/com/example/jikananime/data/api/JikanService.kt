package com.example.jikananime.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanService {
    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int = 1
    ): TopAnimeResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetails(
        @Path("id") id: Int
    ): AnimeDetailsResponse
}

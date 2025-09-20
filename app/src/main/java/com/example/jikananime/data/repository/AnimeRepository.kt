package com.example.jikananime.data.repository

import com.example.jikananime.data.api.AnimeDetailsResponse
import com.example.jikananime.data.api.AnimeSummary
import com.example.jikananime.data.api.JikanService
import com.example.jikananime.data.db.AnimeDao
import com.example.jikananime.data.db.AnimeEntity
import com.example.jikananime.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception

class AnimeRepository(
    private val service: JikanService,
    private val dao: AnimeDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getTopAnime(): Flow<Resource<List<AnimeEntity>>> = flow {
        emit(Resource.Loading)

        val cached = try {
            dao.getAll().firstOrNull() ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        if (cached.isNotEmpty()) {
            emit(Resource.Success(cached))
        }

        try {
            val response = service.getTopAnime()
            val entities = response.data.map { apiToEntity(it) }
            dao.insertAll(entities)
            emit(Resource.Success(entities))
        } catch (e: Exception) {
            if (cached.isNotEmpty()) {
                emit(Resource.Success(cached))
            } else {
                emit(Resource.Error("Failed to fetch: ${e.message}"))
            }
        }
    }.flowOn(ioDispatcher)

    fun getDetails(id: Int): Flow<Resource<AnimeEntity>> = flow {
        emit(Resource.Loading)


        val cached = dao.getById(id).firstOrNull()
        if (cached != null) {
            emit(Resource.Success(cached))
        }

        try {
            val resp: AnimeDetailsResponse = service.getAnimeDetails(id)
            val entity = detailsApiToEntity(resp)

            dao.insert(entity)
            emit(Resource.Success(entity))
        } catch (e: Exception) {
            if (cached != null) {
                emit(Resource.Success(cached))
            } else {
                emit(Resource.Error("Failed to load details: ${e.message}"))
            }
        }
    }.flowOn(ioDispatcher)

    // --- Helper mappers ---
    private fun apiToEntity(api: AnimeSummary): AnimeEntity {
        return AnimeEntity(
            id = api.malId,
            title = api.title,
            episodes = api.episodes,
            score = api.score,
            synopsis = api.synopsis,
            imageUrl = api.images.jpg.imageUrl,
            trailerUrl = api.trailer?.url,
            genres = emptyList()
        )
    }

    private fun detailsApiToEntity(resp: AnimeDetailsResponse): AnimeEntity {
        val anime = resp.data
        return AnimeEntity(
            id = anime.malId,
            title = anime.title,
            episodes = anime.episodes,
            score = anime.score,
            synopsis = anime.synopsis,
            imageUrl = anime.images.jpg.imageUrl,
            trailerUrl = anime.trailer?.url,
            genres = anime.genres.map { it.name }
        )
    }
}

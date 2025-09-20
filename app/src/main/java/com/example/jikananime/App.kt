package com.example.jikananime

import android.app.Application
import com.example.jikananime.data.api.NetworkModule
import com.example.jikananime.data.db.AppDatabase
import com.example.jikananime.data.repository.AnimeRepository

class App : Application() {
    lateinit var repository: AnimeRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getInstance(this)
        repository = AnimeRepository(NetworkModule.jikanService, db.animeDao())
    }
}

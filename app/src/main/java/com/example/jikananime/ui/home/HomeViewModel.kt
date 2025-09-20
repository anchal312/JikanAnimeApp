package com.example.jikananime.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jikananime.data.db.AnimeEntity
import com.example.jikananime.data.repository.AnimeRepository
import com.example.jikananime.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _animeList = MutableStateFlow<Resource<List<AnimeEntity>>>(Resource.Loading)
    val animeList: StateFlow<Resource<List<AnimeEntity>>> = _animeList

    fun fetchTopAnime() {
        viewModelScope.launch {
            repository.getTopAnime().collect { resource ->
                _animeList.value = resource
            }
        }
    }
}


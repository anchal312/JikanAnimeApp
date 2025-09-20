package com.example.jikananime.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jikananime.data.db.AnimeEntity
import com.example.jikananime.data.repository.AnimeRepository
import com.example.jikananime.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _anime = MutableStateFlow<Resource<AnimeEntity>>(Resource.Loading)
    val anime: StateFlow<Resource<AnimeEntity>> = _anime

    fun fetchDetails(animeId: Int) {
        viewModelScope.launch {
            repository.getDetails(animeId).collect { resource ->
                _anime.value = resource
            }
        }
    }
}



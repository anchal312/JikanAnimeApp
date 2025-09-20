package com.example.jikananime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jikananime.ui.detail.DetailViewModel
import com.example.jikananime.ui.home.HomeViewModel
import com.example.jikananime.ui.navigation.AppNavGraph
import com.example.jikananime.ui.theme.JikanAnimeAppTheme
import com.example.jikananime.util.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = (application as App).repository

        setContent {
            JikanAnimeAppTheme {
                val homeVm: HomeViewModel = viewModel(factory = ViewModelFactory { HomeViewModel(repository) })
                val detailVm: DetailViewModel = viewModel(factory = ViewModelFactory { DetailViewModel(repository) })
                AppNavGraph(homeVm, detailVm)
            }
        }
    }
}

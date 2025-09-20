package com.example.jikananime.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.jikananime.data.db.AnimeEntity
import com.example.jikananime.util.Resource

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAnimeClick: (Int) -> Unit
) {
    val animeListState by viewModel.animeList.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.fetchTopAnime()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when (animeListState) {
            is Resource.Loading -> {
                Log.e("HomeScreen", "Loading...")
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }

            is Resource.Success -> {
                val data = (animeListState as Resource.Success<List<AnimeEntity>>).data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    Text(
                        text = "Anime List",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        textAlign = TextAlign.Center
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(items = data) { anime ->
                            AnimeItem(anime = anime, onClick = { onAnimeClick(anime.id) })
                        }
                    }
                }
            }

            is Resource.Error -> {
                val msg = (animeListState as Resource.Error).message
                Text(
                    text = "Error: $msg",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun AnimeItem(anime: AnimeEntity, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp)
            )

            Column {
                Text(
                    text = anime.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Episodes: ${anime.episodes ?: "?"}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Score: ${anime.score ?: "-"}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

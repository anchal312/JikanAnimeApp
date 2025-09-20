package com.example.jikananime.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.example.jikananime.R
import com.example.jikananime.data.db.AnimeEntity
import com.example.jikananime.util.Resource
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun DetailScreen(viewModel: DetailViewModel, animeId: Int) {
    val state by viewModel.anime.collectAsState()

    LaunchedEffect(animeId) { viewModel.fetchDetails(animeId) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is Resource.Loading -> CircularProgressIndicator(color = Color.White)
            is Resource.Error -> Text("Error: ${(state as Resource.Error).message}", color = Color.Red)
            is Resource.Success -> (state as Resource.Success<AnimeEntity>).data?.let { AnimeDetailContent(it) }
        }
    }
}

@Composable
fun AnimeDetailContent(anime: AnimeEntity) {
    val context = LocalContext.current
    val trailerUrl = anime.trailerUrl ?: ""
    var youtubeError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        when {

            (trailerUrl.contains("youtube.com") || trailerUrl.contains("youtu.be")) && !youtubeError -> {
                val videoId = extractYouTubeId(trailerUrl)
                if (videoId != null) {
                    AndroidView(factory = { ctx ->
                        YouTubePlayerView(ctx).apply {
                            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    youTubePlayer.loadVideo(videoId, 0f)
                                }

                                override fun onError(
                                    youTubePlayer: YouTubePlayer,
                                    error: PlayerConstants.PlayerError
                                ) {
                                    youtubeError = true
                                }
                            })
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp))
                } else {
                    youtubeError = true
                }
            }

            youtubeError -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Info message
                    Text(
                        text = "Playing restricted here.. Click to Watch on YouTube",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 12.dp),
                        maxLines = Int.MAX_VALUE
                    )

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(56.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_youtube),
                            contentDescription = "YouTube",
                            modifier = Modifier.size(30.dp),
                            tint = Color.Unspecified
                        )

                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "Watch on YouTube",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

            }

            else -> {
                Image(
                    painter = rememberAsyncImagePainter(anime.imageUrl),
                    contentDescription = anime.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))


        Text(anime.title, style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Spacer(Modifier.height(8.dp))
        Text("Episodes: ${anime.episodes ?: "?"}", color = Color.LightGray)
        Text("Score: ${anime.score ?: "-"}", color = Color.LightGray)
        Text("Genres: ${anime.genres.joinToString()}", color = Color.LightGray)
        Spacer(Modifier.height(12.dp))
        Text(anime.synopsis ?: "No synopsis available.", color = Color.White)
    }
}


fun extractYouTubeId(url: String): String? {
    val regex = "(?<=v=)[^#&?]*".toRegex()
    return regex.find(url)?.value
        ?: url.substringAfterLast("/", missingDelimiterValue = "")
}

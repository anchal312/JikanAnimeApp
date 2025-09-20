package com.example.jikananime.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jikananime.ui.detail.DetailScreen
import com.example.jikananime.ui.detail.DetailViewModel
import com.example.jikananime.ui.home.HomeScreen
import com.example.jikananime.ui.home.HomeViewModel

@Composable
fun AppNavGraph(homeVm: HomeViewModel, detailVm: DetailViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(homeVm) { id ->
                navController.navigate("detail/$id")
            }
        }
        composable(
            route = "detail/{animeId}",
            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("animeId") ?: 0
            DetailScreen(detailVm, id)
        }
    }
}

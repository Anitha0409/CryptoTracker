package com.cryptocurrencytrackerapp.model.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cryptocurrencytrackerapp.view.CoinDetailsScreen
import com.cryptocurrencytrackerapp.view.FavListScreen
import com.cryptocurrencytrackerapp.view.HomeScreen
import com.cryptocurrencytrackerapp.view.SearchScreen
import com.cryptocurrencytrackerapp.viewmodel.CryptoViewModel


@Composable
fun NavigationForCryptoApp(){

    val navController = rememberNavController()
    val cryptoViewModel:CryptoViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = "HomeScreen") {
        composable("HomeScreen"){
            HomeScreen(navController = navController, cryptoViewModel = cryptoViewModel)
        }
        composable("SearchScreen"){
            SearchScreen(navController = navController,cryptoViewModel = cryptoViewModel)
        }

        composable(route = "CoinDetailsScreen/{id}",
                   arguments = listOf(navArgument(name = "id"){type = NavType.StringType})
        ){backstackEntry->
            val id = backstackEntry.arguments?.getString("id")
            CoinDetailsScreen(id = id, cryptoViewModel = cryptoViewModel, navController =navController )
        }

        composable("FavListScreen"){
            FavListScreen( cryptoViewModel = cryptoViewModel, navController = navController)
        }
    }
}


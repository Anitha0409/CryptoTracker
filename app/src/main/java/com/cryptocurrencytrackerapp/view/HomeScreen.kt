package com.cryptocurrencytrackerapp.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cryptocurrencytrackerapp.viewmodel.CryptoViewModel


@Composable
fun HomeScreen(navController: NavController,cryptoViewModel: CryptoViewModel) {

    val listOfCoins by cryptoViewModel.listOfCoins.collectAsState()

    Box(modifier = Modifier.padding(16.dp)) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { navController.navigate("SearchScreen") },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Button")
                }
                Text(
                    text = "List of Coins",
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp
                )
            }

            ListOfCoins(
                coinsInfo = listOfCoins.listOfCoins ?: emptyList(),
                navController = navController,
                cryptoViewModel = cryptoViewModel
            )
        }

            FloatingActionButton(
                onClick = {
                    navController.navigate("FavListScreen")
                },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "List of Fav coins"
                )
            }


    }
}



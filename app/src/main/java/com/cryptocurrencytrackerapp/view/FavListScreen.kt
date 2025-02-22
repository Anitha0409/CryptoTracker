package com.cryptocurrencytrackerapp.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cryptocurrencytrackerapp.viewmodel.CryptoViewModel

@Composable
fun FavListScreen(cryptoViewModel: CryptoViewModel = hiltViewModel(), navController: NavController){

val favList = cryptoViewModel.getFavListOfCoins()
    Column {
        Box(modifier =Modifier.fillMaxWidth() ){
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
            }
            Text(text = "List of your Favourite Coins",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
        }
        if(favList.isNotEmpty()) {
            ListOfCoins(coinsInfo = favList, navController = navController, cryptoViewModel)
        }else{
            Text(text = "You don't have any coins in the favourite list yet, go to details screen to add some")
        }
    }
    }

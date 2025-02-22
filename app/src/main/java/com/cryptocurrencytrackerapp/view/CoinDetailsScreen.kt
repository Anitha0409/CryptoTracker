package com.cryptocurrencytrackerapp.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cryptocurrencytrackerapp.viewmodel.CryptoViewModel

@Composable
fun CoinDetailsScreen(id:String?,cryptoViewModel: CryptoViewModel = hiltViewModel(),navController: NavController){
    val coinDetails by cryptoViewModel.coinDetails.collectAsState()
    Box(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { navController.popBackStack() }, modifier = Modifier.align(
                        Alignment.CenterStart
                    )
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
                }
                if (id != null) {
                    Text(
                        text = id,
                        modifier = Modifier.align(Alignment.Center),
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        fontSize = 20.sp
                    )
                }
            }

            if (!id.isNullOrEmpty()) {
                if (coinDetails.coinDetails != null) {
                    coinDetails.coinDetails.let { coinDetails ->
                        if (coinDetails != null) {
                            CoinByID(coin = coinDetails, cryptoViewModel = cryptoViewModel)
                        }

                    }
                }
            }
        }
    }
    }
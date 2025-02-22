package com.cryptocurrencytrackerapp.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.cryptocurrencytrackerapp.viewmodel.CryptoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController,cryptoViewModel: CryptoViewModel){

    var coinName by remember { mutableStateOf("") }
    val coinDetailInfo by cryptoViewModel.coinDetails.collectAsState()
    val keyBoardController = LocalSoftwareKeyboardController.current

    Scaffold(
       topBar = {
           TopAppBar(
               navigationIcon = { IconButton(onClick = { navController.popBackStack()})
               {
                   Icon(imageVector = Icons.Default.ArrowBack, contentDescription ="Back button" )
               } },
               title = {

                      TextField(value = coinName,
                          onValueChange = {coinName = it},
                          singleLine = true,
                          placeholder = { Text(text = "Search coins by name or id")},
                          modifier = Modifier.fillMaxWidth(),
                          maxLines = 1,
                          keyboardOptions = KeyboardOptions.Default.copy(
                              imeAction = ImeAction.Done
                          ),
                          keyboardActions = KeyboardActions(
                              onDone = {
                                  if (coinName.isNotEmpty()) {
                                      cryptoViewModel.fetchCoinById(coinName)
                                      keyBoardController?.hide()
                                  }
                              }
                                  )
                           )
           }
           )
       }
    ) {innerPadding->

        Box(modifier = Modifier.padding(innerPadding)){
            if(coinName.isNotEmpty()) {
                if (coinDetailInfo.coinDetails != null) {
                    coinDetailInfo.coinDetails?.let { CoinByID(coin = it, cryptoViewModel = cryptoViewModel) }

                }else{
                    Text(text = "Loading or no results found")
                }
            }else{
                Text(text = "Your coin name or id should not be empty")
            }

    }

    }

}
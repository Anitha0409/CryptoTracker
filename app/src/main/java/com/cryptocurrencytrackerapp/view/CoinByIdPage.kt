package com.cryptocurrencytrackerapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cryptocurrencytrackerapp.model.CoinDetails
import com.cryptocurrencytrackerapp.model.Ticker
import com.cryptocurrencytrackerapp.viewmodel.CryptoViewModel

@Composable
fun CoinByID(coin:CoinDetails, cryptoViewModel: CryptoViewModel = hiltViewModel()){

    var isFav by remember{ mutableStateOf(cryptoViewModel.isFav(coin.id)) }


    Column {
        DisplayCoinById(
            id = coin.id,
            name = coin.name,
            symbol = coin.symbol,
            image = coin.image?.small,
            currentPrice = coin.marketData?.currentPrice,
            marketCap = coin.marketData?.marketCaps,
            priceChangePercentageIn24Hour = coin.marketData?.priceChangePercentageIn24Hour,
            tickers = coin.tickers,
            isFav = isFav,
            fabOnClick = {
                cryptoViewModel.toggleFav(coin.id)
                isFav = !isFav
            }
        )
    }
}

@Composable
fun DisplayCoinById(
    id:String,
    name:String,
    symbol:String,
    image:String?,
    currentPrice: Map<String,Double>?,
    marketCap:Map<String,Double>?,
    priceChangePercentageIn24Hour:Double?,
    tickers:List<Ticker>,
    isFav:Boolean,
    fabOnClick:()->Unit
) {
    Box (modifier = Modifier.padding(16.dp)){
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween){
            Text(
                text = buildAnnotatedString {
                    append("The Id is:")
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append(id)
                    pop()
                },
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                text = buildAnnotatedString {
                    append("The name is:")
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append(name)
                    pop()
                },
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                text = buildAnnotatedString {
                    append("The symbol is:")
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append(symbol)
                    pop()
                },
                style = TextStyle(fontSize = 16.sp)
            )
            FloatingActionButton(onClick = { fabOnClick() }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                if (isFav) Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Fav button"
                ) else Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Fav button"
                )
            }
            if (!image.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(model = image), modifier = Modifier
                        .padding(18.dp)
                        .height(150.dp)
                        .fillMaxWidth(), contentScale = ContentScale.Crop, contentDescription = "Image"
                )
            } else {
                Text(
                    text = "Sorry no image available",
                    style = TextStyle(fontSize = 16.sp),
                    fontWeight = FontWeight.Bold
                )
            }

            if (currentPrice != null) {
                if (currentPrice.isNotEmpty()) {
                    currentPrice.let { price ->
                        val priceInUSD = price["usd"] ?: 0.00
                        Text(text = buildAnnotatedString {
                            append("The Current Price is:")
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append(priceInUSD.toString())
                            pop()
                        }, style = TextStyle(fontSize = 16.sp))
                    }
                }
            } else {
                Text(
                    text = "No current price info for this coin",
                    style = TextStyle(fontSize = 16.sp),
                    fontWeight = FontWeight.Bold
                )
            }
            if (marketCap != null) {
                marketCap.let { cap ->
                    val marketCapInUSD = cap["usd"] ?: 0.00
                    Text(
                        text = buildAnnotatedString {
                            append("The market Cap in USD is:")
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append(marketCapInUSD.toString())
                            pop()
                        },
                        style = TextStyle(fontSize = 16.sp)
                    )
                }
            } else {
                Text(text = "No Market cap info for this coin")
            }
            if (priceChangePercentageIn24Hour != null) {
                Text(text = buildAnnotatedString {
                    append("The Price Change in 24 Hours is:")
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append(priceChangePercentageIn24Hour.toString())
                    pop()
                }, style = TextStyle(fontSize = 16.sp))
            } else {
                Text(
                    text = "No price change info present for this coin",
                    style = TextStyle(fontSize = 16.sp),
                    fontWeight = FontWeight.Bold
                )
            }
            if (tickers.isNotEmpty()) {
                LazyColumn {
                    items(tickers) { ticker ->
                        Text(text = buildAnnotatedString {
                            append("The Volume is:")
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append(ticker.volume.toString())
                            pop()
                        }, style = TextStyle(fontSize = 16.sp))
                    }
                }

            }
        }
    }


}
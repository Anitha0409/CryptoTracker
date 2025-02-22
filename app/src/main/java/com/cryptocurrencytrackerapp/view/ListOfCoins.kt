package com.cryptocurrencytrackerapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cryptocurrencytrackerapp.model.CoinsInfo
import com.cryptocurrencytrackerapp.viewmodel.CryptoViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ListOfCoins(coinsInfo: List<CoinsInfo>, navController: NavController, cryptoViewModel: CryptoViewModel = hiltViewModel()){

    val listState = rememberLazyListState()
    val isLoading by cryptoViewModel.isLoadingMore.collectAsState()
    val listOfCoins by cryptoViewModel.listOfCoins.collectAsState()

    if(!listOfCoins.error.isNullOrEmpty()){
        ErrorScreen(error = listOfCoins.error!!)
    }

    LaunchedEffect(listState) {
       snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?:0}
           .distinctUntilChanged()
           .collect{lastVisibleItemIndex->
               if(lastVisibleItemIndex>= coinsInfo.size && !isLoading){
                   cryptoViewModel.loadMoreCoinsIfNeeded(lastVisibleItemIndex = lastVisibleItemIndex)
               }
           }
    }


    LazyColumn (state = listState){
         items(coinsInfo, key = {it.id}) { coin ->
             CoinItem(
                 name = coin.name,
                 id = coin.id,
                 image = coin.image,
                 symbol = coin.symbol,
                 priceChangePercentageIn24Hours = coin.priceChangePercentageIn24Hour,
                 currentPrice = coin.currentPrice,
                 marketCapRank = coin.marketCapRank,
                 lastUpdated = coin.lastUpdated,
                 onClick = {
                     cryptoViewModel.fetchCoinById(id = coin.id)
                     navController.navigate("CoinDetailsScreen/${coin.id}")}
             )

         }
     }
 }


@Composable
fun CoinItem(name:String,
                     id:String,
                     image: String?,
                     symbol:String,
                     priceChangePercentageIn24Hours:Double,
                     currentPrice:Double,
                     marketCapRank:Int,
                     lastUpdated:String?,
                     onClick:()->Unit){

           Column(modifier = Modifier
               .padding(16.dp)
               .clickable { onClick() }) {
              Text(text = buildAnnotatedString {
                  append("The id of the coin is:")
                  pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                  append(id)
                  pop()
              }, style = TextStyle(fontSize = 16.sp))

               Text(text = buildAnnotatedString {
                       append("The name of the coin is:")
                       pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                       append(name)
                       pop() },
                   style = TextStyle(fontSize = 16.sp))

               Text(text = buildAnnotatedString {
                   append("The symbol of the coin is:")
                   pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                   append(symbol)
                   pop()
               }, style = TextStyle(fontSize = 16.sp))

               Text(text = buildAnnotatedString {
                   append("The Price change percentage in 24 hours:")
                   pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                   append(priceChangePercentageIn24Hours.toString())
                   pop()
               }, style = TextStyle(fontSize = 16.sp))

               Text(text = buildAnnotatedString {
                   append("The current Price is:")
                   pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                   append(currentPrice.toString())
                   pop()
               }, style = TextStyle(fontSize = 16.sp))
               Text(text = buildAnnotatedString {
                   append("The market cap rank is:")
                   pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                   append(marketCapRank.toString())
                   pop()
               }, style = TextStyle(fontSize = 16.sp))
               if(!lastUpdated.isNullOrEmpty())    Text(text = buildAnnotatedString {
                   append("The last updated time is:")
                   pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                   append(lastUpdated)
                   pop()
               }, style = TextStyle(fontSize = 16.sp)) else Text(text = "No last updated info available for this coin",style = TextStyle(fontSize = 16.sp), fontWeight = FontWeight.Bold)
               if(!image.isNullOrEmpty()) {
                   Image(painter = rememberAsyncImagePainter(model = image), modifier = Modifier
                       .padding(16.dp)
                       .height(180.dp)
                       .fillMaxWidth(), contentScale = ContentScale.Crop, contentDescription = "Image")

               }else{
                   Text(text = "No image available",style = TextStyle(fontSize = 16.sp), fontWeight = FontWeight.Bold)
               }
          }

}
@Composable
fun ErrorScreen(error:String){

    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally){
        Text(text =error, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp) )

    }
}
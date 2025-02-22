package com.cryptocurrencytrackerapp.repository

import android.util.Log
import com.cryptocurrencytrackerapp.model.CoinDetails
import com.cryptocurrencytrackerapp.model.CoinsInfo
import com.cryptocurrencytrackerapp.model.MarketChart
import com.cryptocurrencytrackerapp.network.ApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject



class CryptoRepo @Inject constructor() {

    suspend fun fetchListOfCoins(page:Int):CryptoResponseData{
        return fetchListOfCoins { ApiService.cryptoApi.getListOfCoins(page=page) }
    }

    suspend fun fetchCoinDetails(id:String):CryptoResponseData{ // Debugging
        return fetchCoinDetails { ApiService.cryptoApi.getCoinData(id = id) }
    }

    suspend fun fetchChartDetails(vsCurrency: String,id:String):CryptoResponseData{
        return fetchChartDetails { ApiService.cryptoApi.getChartData(id = id, vsCurrency = vsCurrency) }

    }
}

private suspend fun fetchListOfCoins(apicall:suspend ()->List<CoinsInfo>):CryptoResponseData{
    return try{
        val listOfCoins = apicall()
        CryptoResponseData.Success.ListOfCoins(listOfCoins)
   }catch (ex:HttpException){
          if(ex.code()==400){
              CryptoResponseData.Failure.NoCoinsFound
          }
          if(ex.code()==429){
              CryptoResponseData.Failure.RateLimitExceeded
          }
          else{
              CryptoResponseData.Failure.UnExpectedError
          }
   }catch (ex:IOException){
       return CryptoResponseData.Failure.NetworkError
   }

}

private suspend fun fetchCoinDetails(apicall: suspend () -> CoinDetails):CryptoResponseData {
    return try {
        val coinDetail = apicall()
        Log.d("API Response", "Received: $coinDetail")  // Debugging
        CryptoResponseData.Success.CoinDetail(coinDetail)
    }catch (ex:HttpException){
        Log.e("API Error", "HTTP Error: ${ex.code()} - ${ex.message()}")
        if(ex.code()==400){
            CryptoResponseData.Failure.NoInfoAboutTheCoin
        }else{
            CryptoResponseData.Failure.UnExpectedError
        }
    }catch (ex:IOException){
        return CryptoResponseData.Failure.NetworkError
    }
}

private suspend fun fetchChartDetails(apicall: suspend () -> MarketChart):CryptoResponseData{
    return try {
        val chartDetail = apicall()
        CryptoResponseData.Success.ChartDetail(chartDetail)
    }catch (ex:HttpException){
        if(ex.code()==400){
            CryptoResponseData.Failure.NoChartInfo
        }else{
            CryptoResponseData.Failure.UnExpectedError
        }
    }catch (ex:IOException){
        return CryptoResponseData.Failure.NetworkError
    }
}
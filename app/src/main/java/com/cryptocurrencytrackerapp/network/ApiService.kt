package com.cryptocurrencytrackerapp.network


import com.cryptocurrencytrackerapp.model.CoinDetails
import com.cryptocurrencytrackerapp.model.CoinsInfo
import com.cryptocurrencytrackerapp.model.MarketChart
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


object ApiService{

private val retrofit = Retrofit.Builder()
                       .baseUrl("https://api.coingecko.com/api/v3/")
                       .addConverterFactory(GsonConverterFactory.create()).build()
    val cryptoApi:CryptoApi = retrofit.create(CryptoApi::class.java)

}


interface CryptoApi{
    @GET("coins/markets")
    suspend fun getListOfCoins(@Query("vs_currency") vsCurrency:String= "usd",
                               @Query("page") page:Int,
                               @Query("per_page")perPage:Int = 10): List<CoinsInfo>

    @GET("coins/{id}")
    suspend fun getCoinData(@Path("id") id:String):CoinDetails

    @GET("coins/{id}/market_chart")
    suspend fun getChartData(@Path("id") id:String,
                             @Query("vs_currency") vsCurrency:String="usd",
                             @Query("days") days:String="7"):MarketChart

}
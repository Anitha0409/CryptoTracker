package com.cryptocurrencytrackerapp.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinsInfo(
    val id:String,
    val name:String,
    val symbol:String,
    @SerializedName("price_change_percentage_24h") val priceChangePercentageIn24Hour:Double,
    val image:String?,
    @SerializedName("current_price") val currentPrice:Double,
    @SerializedName("market_cap_rank") val marketCapRank:Int,
    @SerializedName("last_updated")val lastUpdated:String?
)

@Serializable
data class CoinDetails(
    val id:String,
    val name: String,
    val symbol: String,
    val image: CoinImage?,
    @SerializedName("market_data") val marketData:MarketData?,
    val tickers:List<Ticker>

)

@Serializable
data class CoinImage(
    val small:String? = null
)

@Serializable
data class Ticker(
    val volume:Double
)

@Serializable
data class MarketData(
 @SerializedName("current_price") val currentPrice:Map<String,Double>? = null,
 @SerializedName("market_cap")val marketCaps:Map<String,Double>? = null,
 @SerializedName("price_change_percentage_24h") val priceChangePercentageIn24Hour:Double? = null
)

data class MarketChart(
    val prices: List<List<Double>>
)
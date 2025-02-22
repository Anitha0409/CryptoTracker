package com.cryptocurrencytrackerapp.repository

import com.cryptocurrencytrackerapp.model.CoinDetails
import com.cryptocurrencytrackerapp.model.CoinsInfo
import com.cryptocurrencytrackerapp.model.MarketChart


sealed class CryptoResponseData {

    sealed class Success:CryptoResponseData(){
        data class ListOfCoins(val listOfCoinsInfo: List<CoinsInfo>):Success()
        data class CoinDetail(val coinDetailInfo:CoinDetails):Success()
        data class ChartDetail(val marketChartInfo:MarketChart):Success()

    }

    sealed class Failure:CryptoResponseData(){
        data object NetworkError:Failure()
        data object NoCoinsFound:Failure()
        data object NoInfoAboutTheCoin:Failure()
        data object NoChartInfo:Failure()
        data object UnExpectedError:Failure()
        data object RateLimitExceeded:Failure()
    }


}
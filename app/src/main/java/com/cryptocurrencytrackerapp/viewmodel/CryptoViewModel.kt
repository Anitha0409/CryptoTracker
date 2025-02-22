package com.cryptocurrencytrackerapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cryptocurrencytrackerapp.model.CoinDetails
import com.cryptocurrencytrackerapp.model.CoinsInfo
import com.cryptocurrencytrackerapp.model.MarketChart
import com.cryptocurrencytrackerapp.repository.CryptoRepo
import com.cryptocurrencytrackerapp.repository.CryptoResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CryptoViewModel @Inject constructor (private val cryptoRepo: CryptoRepo):ViewModel() {

    private val _listOfCoins = MutableStateFlow(ListOfCoins())
    val listOfCoins: StateFlow<ListOfCoins> = _listOfCoins

    private val _coinDetail = MutableStateFlow(CoinDetailsInfo())
    val coinDetails: StateFlow<CoinDetailsInfo> = _coinDetail

    private val _favList = MutableStateFlow<List<String>>(emptyList())
    val favList: StateFlow<List<String>> = _favList


    private val _marketChart = MutableStateFlow(MarketChartInfo())
    val marketChart: StateFlow<MarketChartInfo> = _marketChart

    private var currentPage = 1
    private var _isLoadingMore = MutableStateFlow(false)
    var isLoadingMore = _isLoadingMore

    init {
        getListOfCoins()
    }

    fun fetchCoinById(id:String){
       return getCoinDetailInfo(id= id.lowercase())
    }

    fun isFav(id:String):Boolean{
       return _favList.value.contains(id)
    }

    fun getFavListOfCoins():List<CoinsInfo>{
        return _listOfCoins.value.listOfCoins?.filter { it.id in _favList.value } ?: emptyList()
    }

    fun toggleFav(id:String){
        if(!isFav(id))  {
            _favList.value += id
        }
        else{
            _favList.value -=id
        }
    }

    private fun getListOfCoins(){
        _listOfCoins.value = _listOfCoins.value.copy(isLoading = true)
        viewModelScope.launch {
            when(val data = cryptoRepo.fetchListOfCoins(currentPage)){
                is CryptoResponseData.Success.ListOfCoins -> {
                    val oldList = _listOfCoins.value.listOfCoins.orEmpty()
                    val newList = data.listOfCoinsInfo
                    if(newList.isNotEmpty()){
                        val updatedList = oldList + newList
                        _listOfCoins.value = _listOfCoins.value.copy(
                            listOfCoins = updatedList,
                            isLoading = false
                        )
                        currentPage++

                    }else{
                        _listOfCoins.value = _listOfCoins.value.copy(
                            listOfCoins = oldList,
                            isLoading = false
                        )
                        Log.d("Pagination","Old-list size is ${oldList.size}")
                    }
                        isLoadingMore.value = false
                }
                CryptoResponseData.Failure.NetworkError -> {
                    _listOfCoins.value = ListOfCoins(error = "No Internet connection or some kind of network error occurred, try again after sometime")

                }
                CryptoResponseData.Failure.NoCoinsFound -> {

                    _listOfCoins.value = ListOfCoins(error = "No coins found, try something different")
                }
                CryptoResponseData.Failure.UnExpectedError -> {
                    _listOfCoins.value = ListOfCoins(error = "Something unexpected happened, try again")
                }
                CryptoResponseData.Failure.RateLimitExceeded ->{
                    _listOfCoins.value = ListOfCoins(error = "Rate limit exceed, try again after sometime")
                }
                else->{

                }

            }
        }
    }

    fun getCoinDetailInfo(id:String){
            _coinDetail.value = CoinDetailsInfo( isLoading = true)
        viewModelScope.launch {
            try {
                Log.d("API Call", "Fetching coin with ID: $id")  // Debugging
                when(val data = cryptoRepo.fetchCoinDetails(id = id)){
                            is CryptoResponseData.Success.CoinDetail -> {
                        _coinDetail.value = CoinDetailsInfo(
                            isLoading = false,
                            coinDetails = data.coinDetailInfo,
                            error = null)
                        Log.d("SearchResponse", coinDetails.value.toString())

                    }
                    CryptoResponseData.Failure.NetworkError -> {
                        _coinDetail.value = CoinDetailsInfo(error = "No internet connection or some network error, try again after sometime")
                    }
                    CryptoResponseData.Failure.NoInfoAboutTheCoin -> {
                        _coinDetail.value = CoinDetailsInfo(error = "Sorry, no details found for this coin")
                    }
                    CryptoResponseData.Failure.UnExpectedError -> {

                        _coinDetail.value = CoinDetailsInfo(error = "Sorry something unexpected happened")
                    }
                    else -> {
                        //ToDo
                    }
                }
            }catch (e:Exception){
                    Log.e("API Error", "Failed to fetch coin: ${e.message}")
            }
            }

    }

    fun getMarketChart(id:String, vsCurrency: String){
        viewModelScope.launch {
            _marketChart.value = MarketChartInfo(isLoading = true)

            when(val data = cryptoRepo.fetchChartDetails(vsCurrency = vsCurrency, id = id)){

                is CryptoResponseData.Success.ChartDetail -> {
                    _marketChart.value = MarketChartInfo(
                        isLoading = false,
                        marketChart = data.marketChartInfo,
                        error = null)
                }
                CryptoResponseData.Failure.NetworkError -> {
                    _marketChart.value = MarketChartInfo(error = "No internet connection or some network error, try again after sometime")
                }
                CryptoResponseData.Failure.NoChartInfo -> {
                    _marketChart.value = MarketChartInfo(error = "Sorry, no details found for this coin")
                }
                CryptoResponseData.Failure.UnExpectedError -> {
                    _marketChart.value = MarketChartInfo(error = "Sorry something unexpected happened")

                }
                else->{
                    // TODO
                }
            }
        }
    }

    fun loadMoreCoinsIfNeeded(lastVisibleItemIndex:Int){
        val totalItemCount = _listOfCoins.value.listOfCoins?.size?:0
        if(totalItemCount==0) return
        Log.d("Pagination", "Fetching page: $currentPage")
        Log.d("Pagination", "LastVisible: $lastVisibleItemIndex, TotalCount: $totalItemCount")
        if(lastVisibleItemIndex >= totalItemCount-1 && !isLoadingMore.value){
            isLoadingMore.value= true
            getListOfCoins()
        }
    }


    data class ListOfCoins(
        val isLoading: Boolean = false,
        val listOfCoins: List<CoinsInfo>? = null,
        val error: String? = null
    )

    data class CoinDetailsInfo(
        val isLoading: Boolean = false,
        val coinDetails: CoinDetails? = null,
        val error: String? = null
    )

    data class MarketChartInfo(
        val isLoading: Boolean = false,
        val marketChart: MarketChart? = null,
        val error: String? = null
    )

}
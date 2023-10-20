package com.atakandalkiran.stockmarket_composeapp.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.atakandalkiran.stockmarket_composeapp.data.base.BaseViewModel
import com.atakandalkiran.stockmarket_composeapp.domain.repository.MarketStockRepository
import com.atakandalkiran.stockmarket_composeapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MarketStockRepository
): BaseViewModel() {

    var state by mutableStateOf(CompanyListingsState())

    init {
        getCompanyListings()
    }

    fun onEvent(event: CompanyListingsEvent) {
        when(event) {
            is CompanyListingsEvent.Refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }
            is CompanyListingsEvent.OnSearchQueryChange -> {
                state = state.copy(searchKeyword = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListings()
                }
            }
        }
    }

    private fun getCompanyListings(
        query: String = state.searchKeyword.lowercase(),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository.getCompanyListing(fetchFromRemote, query)
                .collectLatest { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let {
                                state = state.copy(
                                    companiesList = it
                                )
                            }
                        }
                        is Resource.Error -> Unit
                        is Resource.Loading ->  {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}
package com.atakandalkiran.stockmarket_composeapp.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.atakandalkiran.stockmarket_composeapp.data.base.BaseViewModel
import com.atakandalkiran.stockmarket_composeapp.domain.repository.MarketStockRepository
import com.atakandalkiran.stockmarket_composeapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MarketStockRepository
) : BaseViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val companyInfoResult = async { repository.getCompanyDetails(symbol) }
            val intradayInfoResult = async { repository.getIntradayInfo(symbol) }
            when(val result = companyInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        companies = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        companies = null,
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> Unit
            }
            when(val result = intradayInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        stockInformation = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        stockInformation = emptyList(),
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> Unit
            }
        }
    }
}
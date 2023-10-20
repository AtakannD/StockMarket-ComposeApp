package com.atakandalkiran.stockmarket_composeapp.screens.home

import com.atakandalkiran.stockmarket_composeapp.domain.model.CompanyListing

data class CompanyListingsState(
    val companiesList: List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchKeyword: String = ""
)

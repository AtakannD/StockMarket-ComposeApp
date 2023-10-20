package com.atakandalkiran.stockmarket_composeapp.screens.detail

import com.atakandalkiran.stockmarket_composeapp.domain.model.CompanyInfo
import com.atakandalkiran.stockmarket_composeapp.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInformation: List<IntradayInfo> = emptyList(),
    val companies: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

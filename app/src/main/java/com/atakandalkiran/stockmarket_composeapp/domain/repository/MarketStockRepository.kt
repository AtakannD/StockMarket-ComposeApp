package com.atakandalkiran.stockmarket_composeapp.domain.repository

import com.atakandalkiran.stockmarket_composeapp.domain.model.CompanyInfo
import com.atakandalkiran.stockmarket_composeapp.domain.model.CompanyListing
import com.atakandalkiran.stockmarket_composeapp.domain.model.IntradayInfo
import com.atakandalkiran.stockmarket_composeapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface MarketStockRepository {

    suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ) : Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(
        symbol: String
    ): Resource<List<IntradayInfo>>

    suspend fun getCompanyDetails(
        symbol: String
    ): Resource<CompanyInfo>
}
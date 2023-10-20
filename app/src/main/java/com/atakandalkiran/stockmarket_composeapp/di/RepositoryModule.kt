package com.atakandalkiran.stockmarket_composeapp.di

import com.atakandalkiran.stockmarket_composeapp.data.csv.CSVParser
import com.atakandalkiran.stockmarket_composeapp.data.csv.CompanyListingsParser
import com.atakandalkiran.stockmarket_composeapp.data.csv.IntradayInfoParser
import com.atakandalkiran.stockmarket_composeapp.data.repository.MarketStockRepositoryImpl
import com.atakandalkiran.stockmarket_composeapp.domain.model.CompanyListing
import com.atakandalkiran.stockmarket_composeapp.domain.model.IntradayInfo
import com.atakandalkiran.stockmarket_composeapp.domain.repository.MarketStockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ): CSVParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindMarketStockRepository(
        stockRepositoryImpl: MarketStockRepositoryImpl
    ): MarketStockRepository
}
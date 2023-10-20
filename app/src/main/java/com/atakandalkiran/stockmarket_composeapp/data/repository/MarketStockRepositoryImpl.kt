package com.atakandalkiran.stockmarket_composeapp.data.repository

import com.atakandalkiran.stockmarket_composeapp.data.csv.CSVParser
import com.atakandalkiran.stockmarket_composeapp.data.csv.CompanyListingsParser
import com.atakandalkiran.stockmarket_composeapp.data.local.MarketStockDao
import com.atakandalkiran.stockmarket_composeapp.data.local.MarketStockDatabase
import com.atakandalkiran.stockmarket_composeapp.data.mapper.toCompanyInfo
import com.atakandalkiran.stockmarket_composeapp.data.mapper.toCompanyListing
import com.atakandalkiran.stockmarket_composeapp.data.mapper.toCompanyListingEntity
import com.atakandalkiran.stockmarket_composeapp.data.remote.MarketStockApi
import com.atakandalkiran.stockmarket_composeapp.domain.model.CompanyInfo
import com.atakandalkiran.stockmarket_composeapp.domain.model.CompanyListing
import com.atakandalkiran.stockmarket_composeapp.domain.model.IntradayInfo
import com.atakandalkiran.stockmarket_composeapp.domain.repository.MarketStockRepository
import com.atakandalkiran.stockmarket_composeapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketStockRepositoryImpl @Inject constructor(
    private val api: MarketStockApi,
    private val database: MarketStockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
) : MarketStockRepository {

    private val marketStockDao = database.dao

    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = marketStockDao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))

            val isDatabaseEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDatabaseEmpty && !fetchFromRemote
            if(shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Please check your internet connection."))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Market Listings can not reachable at this moment."))
                null
            }

            remoteListings?.let { listings ->
                marketStockDao.clearCompanyListings()
                marketStockDao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Success(
                    data = marketStockDao
                        .searchCompanyListing("")
                        .map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val results = intradayInfoParser.parse(response.byteStream())
            Resource.Success(results)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Please check your internet connection.")
        } catch (e:HttpException) {
            e.printStackTrace()
            Resource.Error("Intraday informations can not reachable at this moment.")
        }
    }

    override suspend fun getCompanyDetails(symbol: String): Resource<CompanyInfo> {
        return try {
            val results = api.getCompanyDetails(symbol)
            Resource.Success(results.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Please check your internet connection.")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error("Company details can not reachable at this moment.")
        }
    }
}
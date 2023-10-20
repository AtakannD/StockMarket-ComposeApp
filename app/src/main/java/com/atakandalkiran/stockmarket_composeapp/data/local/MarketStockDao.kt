package com.atakandalkiran.stockmarket_composeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MarketStockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListingEntities: List<CompanyListingEntity>
    )

    @Query(value = "DELETE FROM companylistingentity")
    suspend fun clearCompanyListings()

    @Query("SELECT * FROM companylistingentity WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR UPPER(:query) == symbol")
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>
}
package com.atakandalkiran.stockmarket_composeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CompanyListingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MarketStockDatabase : RoomDatabase() {
    abstract val dao: MarketStockDao
}
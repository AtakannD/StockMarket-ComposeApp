package com.atakandalkiran.stockmarket_composeapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.atakandalkiran.stockmarket_composeapp.data.local.MarketStockDatabase
import com.atakandalkiran.stockmarket_composeapp.data.remote.MarketStockApi
import com.atakandalkiran.stockmarket_composeapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMarketStockApi(): MarketStockApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideMarketStockDatabase(
        @ApplicationContext context: Context
    ): MarketStockDatabase {
        return Room.databaseBuilder(
            context,
            MarketStockDatabase::class.java,
            "marketstockdb.db"
        ).build()
    }
}
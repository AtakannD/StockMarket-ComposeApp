package com.atakandalkiran.stockmarket_composeapp.data.mapper

import com.atakandalkiran.stockmarket_composeapp.data.local.CompanyListingEntity
import com.atakandalkiran.stockmarket_composeapp.data.remote.dto.CompanyDetailDto
import com.atakandalkiran.stockmarket_composeapp.domain.model.CompanyInfo
import com.atakandalkiran.stockmarket_composeapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing() : CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity() : CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyDetailDto.toCompanyInfo() : CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}
package com.atakandalkiran.stockmarket_composeapp.data.mapper

import com.atakandalkiran.stockmarket_composeapp.data.remote.dto.IntradayDto
import com.atakandalkiran.stockmarket_composeapp.domain.model.IntradayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun IntradayDto.toIntradayInfo() : IntradayInfo {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timestamp, formatter)
    return IntradayInfo(
        date = localDateTime,
        close = close
    )
}
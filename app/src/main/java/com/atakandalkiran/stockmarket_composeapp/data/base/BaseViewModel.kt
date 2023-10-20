package com.atakandalkiran.stockmarket_composeapp.data.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {
    var searchJob : Job? = null
}
package com.hastakala.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.hastakala.app.AppConstants
import com.hastakala.app.DateUtils
import com.hastakala.app.data.dao.ColourSum
import com.hastakala.app.data.dao.ProductSum
import com.hastakala.app.data.db.AppDatabase
import com.hastakala.app.data.repository.SaleRepository

class AnalyticsViewModel(application: Application) : AndroidViewModel(application) {

    private val saleRepository: SaleRepository

    private val _selectedPeriod = MutableLiveData(AppConstants.Period.ALL_TIME)
    val selectedPeriod: LiveData<AppConstants.Period> get() = _selectedPeriod

    val colourSums: LiveData<List<ColourSum>>
    val productSums: LiveData<List<ProductSum>>

    init {
        val db = AppDatabase.getDatabase(application)
        saleRepository = SaleRepository(db.saleDao())

        colourSums = _selectedPeriod.switchMap { period ->
            when (period) {
                AppConstants.Period.THIS_WEEK -> saleRepository.getColourSumsThisWeek(DateUtils.getStartOfWeek())
                AppConstants.Period.THIS_MONTH -> saleRepository.getColourSumsThisMonth(DateUtils.getStartOfMonth())
                AppConstants.Period.ALL_TIME -> saleRepository.getColourSumsAllTime()
            }
        }

        productSums = _selectedPeriod.switchMap { period ->
            when (period) {
                AppConstants.Period.THIS_WEEK -> saleRepository.getProductSumsThisWeek(DateUtils.getStartOfWeek())
                AppConstants.Period.THIS_MONTH -> saleRepository.getProductSumsThisMonth(DateUtils.getStartOfMonth())
                AppConstants.Period.ALL_TIME -> saleRepository.getProductSumsAllTime()
            }
        }
    }

    fun setPeriod(period: AppConstants.Period) {
        _selectedPeriod.value = period
    }
}

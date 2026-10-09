package com.hastakala.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hastakala.app.DateUtils
import com.hastakala.app.data.db.AppDatabase
import com.hastakala.app.data.entities.Income
import com.hastakala.app.data.repository.IncomeRepository
import kotlinx.coroutines.launch

class IncomeViewModel(application: Application) : AndroidViewModel(application) {

    private val incomeRepository: IncomeRepository

    val allIncome: LiveData<List<Income>>
    val totalIncomeAllTime: LiveData<Double>
    val totalIncomeThisMonth: LiveData<Double>
    val totalIncomeThisWeek: LiveData<Double>

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> get() = _saveSuccess

    init {
        val db = AppDatabase.getDatabase(application)
        incomeRepository = IncomeRepository(db.incomeDao())
        allIncome = incomeRepository.allIncome
        totalIncomeAllTime = incomeRepository.getTotalIncomeAllTime()
        totalIncomeThisMonth = incomeRepository.getTotalIncomeThisMonth(DateUtils.getStartOfMonth())
        totalIncomeThisWeek = incomeRepository.getTotalIncomeThisWeek(DateUtils.getStartOfWeek())
    }

    fun insertIncome(amount: Double, source: String?, note: String?) {
        viewModelScope.launch {
            try {
                val income = Income(
                    amount = amount,
                    source = source?.takeIf { it.isNotBlank() },
                    note = note?.takeIf { it.isNotBlank() },
                    incomeDate = System.currentTimeMillis()
                )
                incomeRepository.insertIncome(income)
                _saveSuccess.postValue(true)
            } catch (e: Exception) {
                _saveSuccess.postValue(false)
            }
        }
    }
}

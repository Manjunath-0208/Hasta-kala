package com.hastakala.app.data.repository

import androidx.lifecycle.LiveData
import com.hastakala.app.data.dao.IncomeDAO
import com.hastakala.app.data.entities.Income

class IncomeRepository(private val incomeDAO: IncomeDAO) {

    val allIncome: LiveData<List<Income>> = incomeDAO.getAllIncome()

    fun getTotalIncomeAllTime(): LiveData<Double> = incomeDAO.getTotalIncomeAllTime()
    fun getTotalIncomeThisMonth(monthStart: Long): LiveData<Double> = incomeDAO.getTotalIncomeThisMonth(monthStart)
    fun getTotalIncomeThisWeek(weekStart: Long): LiveData<Double> = incomeDAO.getTotalIncomeThisWeek(weekStart)

    suspend fun insertIncome(income: Income): Long {
        return incomeDAO.insertIncome(income)
    }
}

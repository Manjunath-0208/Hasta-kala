package com.hastakala.app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hastakala.app.data.entities.Income

@Dao
interface IncomeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: Income): Long

    @Query("SELECT * FROM income ORDER BY income_date DESC")
    fun getAllIncome(): LiveData<List<Income>>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM income")
    fun getTotalIncomeAllTime(): LiveData<Double>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM income WHERE income_date >= :monthStart")
    fun getTotalIncomeThisMonth(monthStart: Long): LiveData<Double>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM income WHERE income_date >= :weekStart")
    fun getTotalIncomeThisWeek(weekStart: Long): LiveData<Double>
}

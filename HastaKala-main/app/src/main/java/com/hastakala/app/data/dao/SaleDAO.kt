package com.hastakala.app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hastakala.app.data.entities.Sale

data class ColourSum(val colour: String, val total: Int)
data class ProductSum(val productName: String, val total: Int)

@Dao
interface SaleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: Sale): Long

    @Delete
    suspend fun deleteSale(sale: Sale)

    @Query("SELECT * FROM sale ORDER BY sale_date DESC")
    fun getAllSales(): LiveData<List<Sale>>

    // Colour-wise totals — All Time
    @Query("SELECT colour, SUM(quantity) as total FROM sale GROUP BY colour ORDER BY total DESC")
    fun getColourSumsAllTime(): LiveData<List<ColourSum>>

    // Colour-wise totals — This Week
    @Query("SELECT colour, SUM(quantity) as total FROM sale WHERE sale_date >= :weekStart GROUP BY colour ORDER BY total DESC")
    fun getColourSumsThisWeek(weekStart: Long): LiveData<List<ColourSum>>

    // Colour-wise totals — This Month
    @Query("SELECT colour, SUM(quantity) as total FROM sale WHERE sale_date >= :monthStart GROUP BY colour ORDER BY total DESC")
    fun getColourSumsThisMonth(monthStart: Long): LiveData<List<ColourSum>>

    // Product-wise totals — All Time
    @Query("SELECT product_name as productName, SUM(quantity) as total FROM sale GROUP BY product_name ORDER BY total DESC")
    fun getProductSumsAllTime(): LiveData<List<ProductSum>>

    // Product-wise totals — This Week
    @Query("SELECT product_name as productName, SUM(quantity) as total FROM sale WHERE sale_date >= :weekStart GROUP BY product_name ORDER BY total DESC")
    fun getProductSumsThisWeek(weekStart: Long): LiveData<List<ProductSum>>

    // Product-wise totals — This Month
    @Query("SELECT product_name as productName, SUM(quantity) as total FROM sale WHERE sale_date >= :monthStart GROUP BY product_name ORDER BY total DESC")
    fun getProductSumsThisMonth(monthStart: Long): LiveData<List<ProductSum>>

    // Total sale count
    @Query("SELECT COUNT(*) FROM sale")
    fun getTotalSaleCount(): LiveData<Int>
}

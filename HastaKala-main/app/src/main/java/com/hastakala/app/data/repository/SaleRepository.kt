package com.hastakala.app.data.repository

import androidx.lifecycle.LiveData
import com.hastakala.app.data.dao.ColourSum
import com.hastakala.app.data.dao.ProductSum
import com.hastakala.app.data.dao.SaleDAO
import com.hastakala.app.data.entities.Sale

class SaleRepository(private val saleDAO: SaleDAO) {

    val allSales: LiveData<List<Sale>> = saleDAO.getAllSales()
    val totalSaleCount: LiveData<Int> = saleDAO.getTotalSaleCount()

    suspend fun insertSale(sale: Sale): Long {
        return saleDAO.insertSale(sale)
    }

    suspend fun deleteSale(sale: Sale) {
        saleDAO.deleteSale(sale)
    }

    fun getColourSumsAllTime(): LiveData<List<ColourSum>> = saleDAO.getColourSumsAllTime()
    fun getColourSumsThisWeek(weekStart: Long): LiveData<List<ColourSum>> = saleDAO.getColourSumsThisWeek(weekStart)
    fun getColourSumsThisMonth(monthStart: Long): LiveData<List<ColourSum>> = saleDAO.getColourSumsThisMonth(monthStart)

    fun getProductSumsAllTime(): LiveData<List<ProductSum>> = saleDAO.getProductSumsAllTime()
    fun getProductSumsThisWeek(weekStart: Long): LiveData<List<ProductSum>> = saleDAO.getProductSumsThisWeek(weekStart)
    fun getProductSumsThisMonth(monthStart: Long): LiveData<List<ProductSum>> = saleDAO.getProductSumsThisMonth(monthStart)
}

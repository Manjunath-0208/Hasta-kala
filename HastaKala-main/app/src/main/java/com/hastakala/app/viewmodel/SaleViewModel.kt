package com.hastakala.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hastakala.app.data.db.AppDatabase
import com.hastakala.app.data.entities.Sale
import com.hastakala.app.data.repository.InventoryRepository
import com.hastakala.app.data.repository.SaleRepository
import kotlinx.coroutines.launch

data class StockAlertEvent(val product: String, val colour: String, val remaining: Int)

class SaleViewModel(application: Application) : AndroidViewModel(application) {

    private val saleRepository: SaleRepository
    private val inventoryRepository: InventoryRepository

    val allSales: LiveData<List<Sale>>
    val totalSaleCount: LiveData<Int>

    private val _stockAlert = MutableLiveData<StockAlertEvent?>()
    val stockAlert: LiveData<StockAlertEvent?> get() = _stockAlert

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> get() = _saveSuccess

    init {
        val db = AppDatabase.getDatabase(application)
        saleRepository = SaleRepository(db.saleDao())
        inventoryRepository = InventoryRepository(db.inventoryDao())
        allSales = saleRepository.allSales
        totalSaleCount = saleRepository.totalSaleCount
    }

    fun insertSale(product: String, colour: String, quantity: Int) {
        viewModelScope.launch {
            try {
                val sale = Sale(
                    productName = product,
                    colour = colour,
                    quantity = quantity,
                    saleDate = System.currentTimeMillis()
                )
                saleRepository.insertSale(sale)

                // Decrement stock and check for low-stock alert
                val newQty = inventoryRepository.decrementStockAndGetNew(product, colour, quantity)
                if (newQty != null && newQty < 3) {
                    _stockAlert.postValue(StockAlertEvent(product, colour, newQty))
                } else {
                    _stockAlert.postValue(null)
                }

                _saveSuccess.postValue(true)
            } catch (e: Exception) {
                _saveSuccess.postValue(false)
            }
        }
    }

    fun deleteSale(sale: Sale) {
        viewModelScope.launch {
            saleRepository.deleteSale(sale)
        }
    }

    fun clearStockAlert() {
        _stockAlert.value = null
    }
}

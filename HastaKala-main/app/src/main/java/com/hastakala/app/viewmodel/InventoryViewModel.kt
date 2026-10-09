package com.hastakala.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hastakala.app.data.db.AppDatabase
import com.hastakala.app.data.entities.Inventory
import com.hastakala.app.data.repository.InventoryRepository
import kotlinx.coroutines.launch

class InventoryViewModel(application: Application) : AndroidViewModel(application) {

    private val inventoryRepository: InventoryRepository
    val allInventory: LiveData<List<Inventory>>

    init {
        val db = AppDatabase.getDatabase(application)
        inventoryRepository = InventoryRepository(db.inventoryDao())
        allInventory = inventoryRepository.allInventory
    }

    fun setStock(product: String, colour: String, quantity: Int) {
        viewModelScope.launch {
            val inventory = Inventory(
                productName = product,
                colour = colour,
                stockQuantity = quantity
            )
            inventoryRepository.insertOrUpdate(inventory)
        }
    }
}

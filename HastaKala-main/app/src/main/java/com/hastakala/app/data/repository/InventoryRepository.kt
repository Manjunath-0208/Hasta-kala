package com.hastakala.app.data.repository

import androidx.lifecycle.LiveData
import com.hastakala.app.data.dao.InventoryDAO
import com.hastakala.app.data.entities.Inventory

class InventoryRepository(private val inventoryDAO: InventoryDAO) {

    val allInventory: LiveData<List<Inventory>> = inventoryDAO.getAllInventory()

    suspend fun insertOrUpdate(inventory: Inventory) {
        inventoryDAO.insertOrUpdate(inventory)
    }

    suspend fun getInventoryItem(product: String, colour: String): Inventory? {
        return inventoryDAO.getInventoryItem(product, colour)
    }

    /**
     * Decrements stock and returns the new quantity after decrement.
     * Returns null if no inventory record exists for this combination.
     */
    suspend fun decrementStockAndGetNew(product: String, colour: String, qty: Int): Int? {
        val rowsUpdated = inventoryDAO.decrementStock(product, colour, qty)
        return if (rowsUpdated > 0) {
            inventoryDAO.getStockQuantity(product, colour)
        } else {
            null
        }
    }
}

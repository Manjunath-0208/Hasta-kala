package com.hastakala.app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hastakala.app.data.entities.Inventory

@Dao
interface InventoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(inventory: Inventory)

    @Query("SELECT * FROM inventory ORDER BY product_name ASC, colour ASC")
    fun getAllInventory(): LiveData<List<Inventory>>

    @Query("SELECT * FROM inventory WHERE product_name = :product AND colour = :colour LIMIT 1")
    suspend fun getInventoryItem(product: String, colour: String): Inventory?

    @Query("UPDATE inventory SET stock_quantity = stock_quantity - :qty WHERE product_name = :product AND colour = :colour AND stock_quantity >= :qty")
    suspend fun decrementStock(product: String, colour: String, qty: Int): Int

    @Query("SELECT stock_quantity FROM inventory WHERE product_name = :product AND colour = :colour LIMIT 1")
    suspend fun getStockQuantity(product: String, colour: String): Int?
}

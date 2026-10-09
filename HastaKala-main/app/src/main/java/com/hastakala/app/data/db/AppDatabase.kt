package com.hastakala.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hastakala.app.data.dao.IncomeDAO
import com.hastakala.app.data.dao.InventoryDAO
import com.hastakala.app.data.dao.SaleDAO
import com.hastakala.app.data.entities.Income
import com.hastakala.app.data.entities.Inventory
import com.hastakala.app.data.entities.Sale

@Database(
    entities = [Sale::class, Inventory::class, Income::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun saleDao(): SaleDAO
    abstract fun inventoryDao(): InventoryDAO
    abstract fun incomeDao(): IncomeDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hastakala_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

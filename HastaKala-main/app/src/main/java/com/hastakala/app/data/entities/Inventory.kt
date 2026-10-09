package com.hastakala.app.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "inventory",
    indices = [Index(value = ["product_name", "colour"], unique = true)]
)
data class Inventory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "product_name")
    val productName: String,

    @ColumnInfo(name = "colour")
    val colour: String,

    @ColumnInfo(name = "stock_quantity")
    val stockQuantity: Int = 0
)

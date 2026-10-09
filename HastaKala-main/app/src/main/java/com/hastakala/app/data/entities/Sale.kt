package com.hastakala.app.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sale")
data class Sale(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "product_name")
    val productName: String,

    @ColumnInfo(name = "colour")
    val colour: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "sale_date")
    val saleDate: Long = System.currentTimeMillis()
)

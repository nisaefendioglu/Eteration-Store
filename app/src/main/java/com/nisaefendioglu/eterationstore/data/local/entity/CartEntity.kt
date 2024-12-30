package com.nisaefendioglu.eterationstore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val name : String,
    val price : String,
    val count: Int
){
    val totalPrice get() = (price.toDouble())*count
}
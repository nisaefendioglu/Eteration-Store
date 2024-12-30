package com.nisaefendioglu.eterationstore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_products")
data class FavoriteProduct(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: String,
    val image : String,
    val name : String,
    val price : String
)
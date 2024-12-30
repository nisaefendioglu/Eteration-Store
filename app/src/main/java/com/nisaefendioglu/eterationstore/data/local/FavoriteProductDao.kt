package com.nisaefendioglu.eterationstore.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nisaefendioglu.eterationstore.data.local.entity.FavoriteProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteProduct: FavoriteProduct)

    @Query("DELETE FROM favorite_products WHERE productId = :productId")
    suspend fun delete(productId: String)

    @Query("SELECT * FROM favorite_products")
    fun getAllFavorites(): Flow<List<FavoriteProduct>>

    @Query("SELECT * FROM favorite_products WHERE productId = :productId")
    fun isFavorite(productId: String): Flow<FavoriteProduct?>
}
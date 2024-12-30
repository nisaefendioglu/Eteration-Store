package com.nisaefendioglu.eterationstore.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nisaefendioglu.eterationstore.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cartEntity: CartEntity)

    @Delete
    suspend fun deleteCart(cartEntity: CartEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCart(cartEntity: CartEntity)
    @Query("SELECT * FROM cart_table")
     fun getAllCartEntities(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart_table WHERE productId = :productId LIMIT 1")
    suspend fun getCartItemByProductId(productId: Int): CartEntity?
    @Query("DELETE FROM cart_table")
    suspend fun deleteAllEntities()
}
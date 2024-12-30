package com.nisaefendioglu.eterationstore.domain.repository

import com.nisaefendioglu.eterationstore.data.local.entity.FavoriteProduct
import kotlinx.coroutines.flow.Flow

interface FavoriteProductRepository {
    suspend fun insert(favoriteProduct: FavoriteProduct)
    suspend fun delete(productId: String)
    fun getAllFavorites(): Flow<List<FavoriteProduct>>
    fun isFavorite(productId: String): Flow<FavoriteProduct?>
}
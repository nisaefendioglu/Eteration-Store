package com.nisaefendioglu.eterationstore.data.repository

import com.nisaefendioglu.eterationstore.data.local.FavoriteProductDao
import com.nisaefendioglu.eterationstore.data.local.entity.FavoriteProduct
import com.nisaefendioglu.eterationstore.domain.repository.FavoriteProductRepository
import kotlinx.coroutines.flow.Flow

class FavoriteProductRepositoryImpl(private val favoriteProductDao: FavoriteProductDao) : FavoriteProductRepository {
    override suspend fun insert(favoriteProduct: FavoriteProduct) {
        favoriteProductDao.insert(favoriteProduct)
    }
    override suspend fun delete(productId: String) {
        favoriteProductDao.delete(productId)
    }
    override fun getAllFavorites(): Flow<List<FavoriteProduct>> {
        return favoriteProductDao.getAllFavorites()
    }
    override fun isFavorite(productId: String): Flow<FavoriteProduct?> {
        return favoriteProductDao.isFavorite(productId)
    }
}

package com.nisaefendioglu.eterationstore.data.repository

import com.nisaefendioglu.eterationstore.data.local.CartDao
import com.nisaefendioglu.eterationstore.data.local.entity.CartEntity
import com.nisaefendioglu.eterationstore.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class CartRepositoryImpl(private val cartDao: CartDao) : CartRepository {
    override suspend fun insertCartEntity(cartEntity: CartEntity) {
        cartDao.getCartItemByProductId(cartEntity.productId)?.let { existingCartEntity ->
            cartDao.updateCart(existingCartEntity.copy(count = existingCartEntity.count + 1))
        } ?: run {
            cartDao.insertCart(cartEntity)
        }
    }

    override suspend fun deleteCartEntity(cartEntity: CartEntity) {
        cartDao.deleteCart(cartEntity)
    }

    override suspend fun updateCartEntity(cartEntity: CartEntity) {
        cartDao.updateCart(cartEntity)
    }

    override suspend fun completeCart() {
        cartDao.deleteAllEntities()
    }
    override fun getAllCartEntities(): Flow<List<CartEntity>> {
        return cartDao.getAllCartEntities()
    }
}

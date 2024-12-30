package com.nisaefendioglu.eterationstore.domain.repository

import com.nisaefendioglu.eterationstore.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun insertCartEntity(cartEntity: CartEntity)
    suspend fun deleteCartEntity(cartEntity: CartEntity)
    suspend fun updateCartEntity(cartEntity: CartEntity)
    suspend fun completeCart()
    fun getAllCartEntities() : Flow<List<CartEntity>>
}
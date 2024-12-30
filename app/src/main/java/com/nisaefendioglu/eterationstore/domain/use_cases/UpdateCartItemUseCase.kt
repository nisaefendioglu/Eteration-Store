package com.nisaefendioglu.eterationstore.domain.use_cases

import com.nisaefendioglu.eterationstore.data.local.entity.CartEntity
import com.nisaefendioglu.eterationstore.data.repository.CartRepositoryImpl
import javax.inject.Inject

class UpdateCartItemUseCase @Inject constructor(private val cartRepositoryImpl: CartRepositoryImpl) {
    suspend  operator fun invoke(cartEntity: CartEntity)=cartRepositoryImpl.updateCartEntity(cartEntity)
}
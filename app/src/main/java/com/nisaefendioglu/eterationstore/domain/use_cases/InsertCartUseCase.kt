package com.nisaefendioglu.eterationstore.domain.use_cases

import com.nisaefendioglu.eterationstore.data.local.entity.CartEntity
import com.nisaefendioglu.eterationstore.data.repository.CartRepositoryImpl
import com.nisaefendioglu.eterationstore.domain.models.Product
import javax.inject.Inject

class InsertCartUseCase @Inject constructor(private val cartRepositoryImpl: CartRepositoryImpl) {
   suspend  operator fun invoke(product: Product){
      product.id?.let {
         val cartEntity = CartEntity(
            productId = it.toInt(),
            name = product.name.toString(),
            price = product.price.toString(),
            count = 1
         )
         cartRepositoryImpl.insertCartEntity(cartEntity)
      }
   }
}
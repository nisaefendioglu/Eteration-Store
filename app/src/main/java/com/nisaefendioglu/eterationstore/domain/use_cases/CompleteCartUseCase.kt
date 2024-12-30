package com.nisaefendioglu.eterationstore.domain.use_cases

import com.nisaefendioglu.eterationstore.data.repository.CartRepositoryImpl
import javax.inject.Inject

class CompleteCartUseCase @Inject constructor(private val cartRepositoryImpl: CartRepositoryImpl) {
   suspend operator fun invoke() = cartRepositoryImpl.completeCart()
}
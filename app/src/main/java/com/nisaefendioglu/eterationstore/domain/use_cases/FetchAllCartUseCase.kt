package com.nisaefendioglu.eterationstore.domain.use_cases

import com.nisaefendioglu.eterationstore.data.repository.CartRepositoryImpl
import javax.inject.Inject

class FetchAllCartUseCase @Inject constructor(private val cartRepositoryImpl: CartRepositoryImpl) {
    operator fun invoke()=cartRepositoryImpl.getAllCartEntities()
}
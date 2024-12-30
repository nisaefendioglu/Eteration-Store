package com.nisaefendioglu.eterationstore.presentation.cart

import androidx.lifecycle.viewModelScope
import com.nisaefendioglu.eterationstore.core.UIEffect
import com.nisaefendioglu.eterationstore.core.UIEvent
import com.nisaefendioglu.eterationstore.core.UIState
import com.nisaefendioglu.eterationstore.data.local.entity.CartEntity
import com.nisaefendioglu.eterationstore.domain.use_cases.CompleteCartUseCase
import com.nisaefendioglu.eterationstore.domain.use_cases.DeleteCartUseCase
import com.nisaefendioglu.eterationstore.domain.use_cases.FetchAllCartUseCase
import com.nisaefendioglu.eterationstore.domain.use_cases.UpdateCartItemUseCase
import com.nisaefendioglu.eterationstore.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val getAllCartEntitiesUseCase: FetchAllCartUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val completeCartUseCase: CompleteCartUseCase
) : BaseViewModel<CartUIEvent, CartUIState, CartUIEffect>() {
    override fun createInitialState(): CartUIState {
        return CartUIState()
    }

    override fun handleEvent(event: CartUIEvent) {
        when (event) {
            is CartUIEvent.OnGetAllCarts -> getAllCarts()
            is CartUIEvent.OnAddCartEntity -> addCart(event.cartEntity)
            is CartUIEvent.OnCompleteCart -> completeCart()
            is CartUIEvent.OnMinusCartCount -> minusCartEntityCount(event.cartEntity)
            is CartUIEvent.OnPlusCartCount -> plusCartEntityCount(event.cartEntity)
        }
    }


    private fun getAllCarts() {
        viewModelScope.launch {
            getAllCartEntitiesUseCase().collect { cartEntities ->
                val totalPrice = cartEntities.sumOf { it.totalPrice }.toInt()
                setState {
                    copy(
                        cartList = cartEntities.ifEmpty { emptyList() },
                        totalPrice = totalPrice
                    )
                }
            }
        }
    }

    private fun addCart(cartEntity: CartEntity) {
        viewModelScope.launch {
            updateCartItemUseCase(cartEntity)
        }
    }

    private fun completeCart() {
        viewModelScope.launch {
            completeCartUseCase()
            setEffect { CartUIEffect.ShowSnackBar }
        }
    }

    private fun minusCartEntityCount(cartEntity: CartEntity) {
        viewModelScope.launch {
            if (cartEntity.count > 1) {
                updateCartItemUseCase(cartEntity.copy(count = cartEntity.count - 1))
            } else {
                deleteCartUseCase(cartEntity)
            }
        }
    }

    private fun plusCartEntityCount(cartEntity: CartEntity) {
        viewModelScope.launch {
            updateCartItemUseCase(cartEntity.copy(count = cartEntity.count + 1))
        }
    }
}

data class CartUIState(
    val cartList: List<CartEntity>? = null,
    val totalPrice: Int = 0
) : UIState

sealed class CartUIEvent : UIEvent {
    data object OnGetAllCarts : CartUIEvent()
    data class OnAddCartEntity(val cartEntity: CartEntity) : CartUIEvent()
    data object OnCompleteCart : CartUIEvent()
    data class OnMinusCartCount(val cartEntity: CartEntity) : CartUIEvent()
    data class OnPlusCartCount(val cartEntity: CartEntity) : CartUIEvent()
}

sealed class CartUIEffect : UIEffect {
    data object ShowSnackBar : CartUIEffect()
}
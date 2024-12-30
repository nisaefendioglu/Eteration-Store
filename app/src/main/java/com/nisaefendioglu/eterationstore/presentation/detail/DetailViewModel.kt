package com.nisaefendioglu.eterationstore.presentation.detail

import androidx.lifecycle.viewModelScope
import com.nisaefendioglu.eterationstore.core.BaseViewModel
import com.nisaefendioglu.eterationstore.data.local.entity.FavoriteProduct
import com.nisaefendioglu.eterationstore.data.repository.FavoriteProductRepositoryImpl
import com.nisaefendioglu.eterationstore.domain.models.Product
import com.nisaefendioglu.eterationstore.domain.use_cases.InsertCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val insertCartUseCase: InsertCartUseCase,
    private val favoriteProductRepositoryImpl: FavoriteProductRepositoryImpl
) : BaseViewModel<DetailUIEvent, DetailUIState, DetailUIEffect>() {

    override fun createInitialState(): DetailUIState = DetailUIState()

    override fun handleEvent(event: DetailUIEvent) {
        when (event) {
            is DetailUIEvent.OnAddCartEntity -> addCartEntity()
            is DetailUIEvent.OnAddFavorite -> addFavorite()
            is DetailUIEvent.OnDeleteFavorite -> deleteFavorite()
            is DetailUIEvent.OnCheckFavorite -> checkFavorite()
            is DetailUIEvent.OnSetProduct -> setProduct(event.product)
        }
    }

    private fun addCartEntity() {
        viewModelScope.launch {
            uiState.value.product?.let {
                insertCartUseCase(it)
                setEffect { DetailUIEffect.ShowSnackBar("Added ${it.brand} to cart") }
            }
        }
    }

    private fun addFavorite() {
        val product = uiState.value.product
        if (product != null) {
            val productId = product.id?.toIntOrNull()
            val productImage = product.image
            val productPrice = product.price
            val productBrand = product.brand

            if (productId != null && productImage != null && productPrice != null && productBrand != null) {
                viewModelScope.launch {
                    favoriteProductRepositoryImpl.insert(
                        FavoriteProduct(
                            productId = productId.toString(),
                            image = productImage,
                            price = productPrice,
                            name = productBrand
                        )
                    )
                    setEffect { DetailUIEffect.ShowSnackBar("$productBrand added to favorites.") }
                }
            } else {
                setEffect { DetailUIEffect.ShowSnackBar("Failed to add to favorites. Invalid product data.") }
            }
        }
    }

    private fun deleteFavorite() {
        uiState.value.product?.id?.toString()?.let { productId ->
            viewModelScope.launch {
                favoriteProductRepositoryImpl.delete(productId)
                setEffect { DetailUIEffect.ShowSnackBar("Removed from favorites.") }
            }
        }
    }

    private fun checkFavorite() {
        val productId = uiState.value.product?.id
        if (productId != null) {
            viewModelScope.launch {
                favoriteProductRepositoryImpl.isFavorite(productId).collect { favProduct ->
                    setState { copy(isFav = favProduct != null) }
                }
            }
        }
    }

    private fun setProduct(product: Product) {
        setState { copy(product = product) }
        checkFavorite()
    }
}

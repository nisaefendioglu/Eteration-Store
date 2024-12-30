package com.nisaefendioglu.eterationstore.presentation.favorites

import androidx.lifecycle.viewModelScope
import com.nisaefendioglu.eterationstore.core.BaseViewModel
import com.nisaefendioglu.eterationstore.data.local.entity.FavoriteProduct
import com.nisaefendioglu.eterationstore.data.repository.FavoriteProductRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteProductRepositoryImpl: FavoriteProductRepositoryImpl
) : BaseViewModel<FavoritesUIEvent, FavoritesUIState, FavoritesUIEffect>() {

    override fun createInitialState(): FavoritesUIState = FavoritesUIState()

    override fun handleEvent(event: FavoritesUIEvent) {
        when (event) {
            is FavoritesUIEvent.OnGetAllFavorites -> getAllFavorites()
            is FavoritesUIEvent.OnDeleteFavorite -> deleteFavorite(event.favoriteProduct)
        }
    }

    private fun getAllFavorites() {
        viewModelScope.launch {
            favoriteProductRepositoryImpl.getAllFavorites().collect { favorites ->
                setState { copy(favoriteList = favorites) }
            }
        }
    }

    private fun deleteFavorite(favoriteProduct: FavoriteProduct) {
        viewModelScope.launch {
            favoriteProductRepositoryImpl.delete(favoriteProduct.productId)
            setEffect { FavoritesUIEffect.ShowSnackBar(favoriteProduct.name) }
        }
    }
}

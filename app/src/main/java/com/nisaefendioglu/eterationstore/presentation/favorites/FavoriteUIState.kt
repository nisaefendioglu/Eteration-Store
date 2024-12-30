package com.nisaefendioglu.eterationstore.presentation.favorites

import com.nisaefendioglu.eterationstore.core.UIEffect
import com.nisaefendioglu.eterationstore.core.UIEvent
import com.nisaefendioglu.eterationstore.core.UIState
import com.nisaefendioglu.eterationstore.data.local.entity.FavoriteProduct

data class FavoritesUIState(
    val favoriteList: List<FavoriteProduct>? = null
) : UIState

sealed class FavoritesUIEvent : UIEvent {
    data object OnGetAllFavorites : FavoritesUIEvent()
    data class OnDeleteFavorite(val favoriteProduct: FavoriteProduct) : FavoritesUIEvent()
}

sealed class FavoritesUIEffect : UIEffect {
    data class ShowSnackBar(val message: String) : FavoritesUIEffect()
}

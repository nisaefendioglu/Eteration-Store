package com.nisaefendioglu.eterationstore.presentation.detail

import com.nisaefendioglu.eterationstore.core.UIEffect
import com.nisaefendioglu.eterationstore.core.UIEvent
import com.nisaefendioglu.eterationstore.core.UIState
import com.nisaefendioglu.eterationstore.domain.models.Product

data class DetailUIState(
    val isFav: Boolean = false,
    val product: Product? = null
) : UIState

sealed class DetailUIEffect : UIEffect {
    data class ShowSnackBar(val message: String) : DetailUIEffect()
}

sealed class DetailUIEvent : UIEvent {
    data class OnSetProduct(val product: Product) : DetailUIEvent()
    data object OnAddCartEntity : DetailUIEvent()
    data object OnAddFavorite : DetailUIEvent()
    data object OnDeleteFavorite : DetailUIEvent()
    data object OnCheckFavorite : DetailUIEvent()
}

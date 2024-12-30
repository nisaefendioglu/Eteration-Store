package com.nisaefendioglu.eterationstore.presentation.profile

import com.nisaefendioglu.eterationstore.core.UIEffect
import com.nisaefendioglu.eterationstore.core.UIEvent
import com.nisaefendioglu.eterationstore.core.UIState

data class ProfileUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) : UIState

sealed class ProfileUIEvent : UIEvent {
    data object OnDefaultEvent : ProfileUIEvent()
}

sealed class ProfileUIEffect : UIEffect {
    data class ShowToast(val message: String) : ProfileUIEffect()
}

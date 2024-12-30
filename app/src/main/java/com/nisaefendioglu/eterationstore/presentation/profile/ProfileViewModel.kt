package com.nisaefendioglu.eterationstore.presentation.profile

import com.nisaefendioglu.eterationstore.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() :
    BaseViewModel<ProfileUIEvent, ProfileUIState, ProfileUIEffect>() {

    override fun createInitialState() = ProfileUIState()

    override fun handleEvent(event: ProfileUIEvent) {
        when (event) {
            is ProfileUIEvent.OnDefaultEvent -> handleDefaultEvent()
        }
    }

    private fun handleDefaultEvent() {
        setEffect { ProfileUIEffect.ShowToast("Default event triggered!") }

    }
}
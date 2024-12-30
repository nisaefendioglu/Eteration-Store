package com.nisaefendioglu.eterationstore.presentation.profile

import androidx.fragment.app.viewModels
import com.nisaefendioglu.eterationstore.core.BaseFragment
import com.nisaefendioglu.eterationstore.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileUIEvent, ProfileUIState, ProfileUIEffect, ProfileViewModel>(
        FragmentProfileBinding::inflate
    ) {
    override val viewModel: ProfileViewModel by viewModels()

    override fun handleEffect(effect: ProfileUIEffect) {

    }

    override fun observeState(state: ProfileUIState) {
    }
}
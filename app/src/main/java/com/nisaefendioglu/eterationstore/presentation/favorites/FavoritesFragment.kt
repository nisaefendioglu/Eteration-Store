package com.nisaefendioglu.eterationstore.presentation.favorites

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nisaefendioglu.eterationstore.R
import com.nisaefendioglu.eterationstore.core.BaseFragment
import com.nisaefendioglu.eterationstore.databinding.FragmentFavoritesBinding
import com.nisaefendioglu.eterationstore.utils.changeVisibility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment :
    BaseFragment<FragmentFavoritesBinding, FavoritesUIEvent, FavoritesUIState, FavoritesUIEffect, FavoritesViewModel>(
        FragmentFavoritesBinding::inflate
    ) {
    private lateinit var favoriteAdapter: FavoriteProductAdapter
    override val viewModel: FavoritesViewModel by viewModels()

    override fun observeState(state: FavoritesUIState) {
        binding.favoriteEmptyText.changeVisibility(state.favoriteList.isNullOrEmpty())
        favoriteAdapter.submitList(state.favoriteList)
    }

    override fun handleEffect(effect: FavoritesUIEffect) {
        if (effect is FavoritesUIEffect.ShowSnackBar) {
            Snackbar.make(
                binding.root,
                "${effect.message} ${resources.getText(R.string.deleted)}",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun setupUI() {
        super.setupUI()
        viewModel.setEvent(FavoritesUIEvent.OnGetAllFavorites)
        favoriteAdapter = FavoriteProductAdapter {
            viewModel.setEvent(FavoritesUIEvent.OnDeleteFavorite(it))
        }
        binding.favoriteRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
        }
    }
}

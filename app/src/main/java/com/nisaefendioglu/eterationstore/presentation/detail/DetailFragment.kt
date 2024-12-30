package com.nisaefendioglu.eterationstore.presentation.detail


import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nisaefendioglu.eterationstore.R
import com.nisaefendioglu.eterationstore.core.BaseFragment
import com.nisaefendioglu.eterationstore.utils.clickWithDebounce
import com.nisaefendioglu.eterationstore.utils.getImageFromUrl
import com.nisaefendioglu.eterationstore.databinding.FragmentDetailBinding
import com.nisaefendioglu.eterationstore.domain.models.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding, DetailUIEvent, DetailUIState, DetailUIEffect, DetailViewModel>(FragmentDetailBinding::inflate) {
    override val viewModel: DetailViewModel by viewModels()

    override fun handleEffect(effect: DetailUIEffect) {
        when (effect) {
            is DetailUIEffect.ShowSnackBar -> {
                Snackbar.make(binding.root, "${effect.message}", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun observeState(state: DetailUIState) {
        binding.apply {
            productDetailFavoriteStar.isSelected = state.isFav
            productDetailFavoriteStar.clickWithDebounce {
                if (state.isFav) viewModel.setEvent(DetailUIEvent.OnDeleteFavorite)
                else viewModel.setEvent(DetailUIEvent.OnAddFavorite)
            }
        }
    }

    override fun setupUI() {
        super.setupUI()
        arguments?.let {
            val product = DetailFragmentArgs.fromBundle(it).product
            viewModel.setEvent(DetailUIEvent.OnSetProduct(product))
            updateUI(product)
        }
    }

    private fun updateUI(product: Product) {
        binding.apply {
            productDetailToolbar.title = product.brand
            productDetailPriceText.text = "${product.price} ${resources.getText(R.string.priceIcon)}"
            productDetailName.text = product.brand
            productDetailDescription.text = product.description
            productDetailImage.getImageFromUrl(product.image)
        }
    }

    override fun setupListeners() {
        binding.apply {
            productDetailToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            productDetailButton.clickWithDebounce { viewModel.setEvent(DetailUIEvent.OnAddCartEntity) }
        }
    }
}

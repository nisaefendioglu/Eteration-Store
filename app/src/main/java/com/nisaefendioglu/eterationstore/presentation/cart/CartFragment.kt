package com.nisaefendioglu.eterationstore.presentation.cart

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nisaefendioglu.eterationstore.R
import com.nisaefendioglu.eterationstore.core.BaseFragment
import com.nisaefendioglu.eterationstore.utils.changeVisibility
import com.nisaefendioglu.eterationstore.utils.clickWithDebounce
import com.nisaefendioglu.eterationstore.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment :
    BaseFragment<FragmentCartBinding, CartUIEvent, CartUIState, CartUIEffect, CartViewModel>(
        FragmentCartBinding::inflate
    ) {
    override val viewModel: CartViewModel by viewModels()


    override fun handleEffect(effect: CartUIEffect) {
        when (effect) {
            is CartUIEffect.ShowSnackBar -> {
                Snackbar.make(
                    binding.root,
                    resources.getText(R.string.complete_successful),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun observeState(state: CartUIState) {
        val cartAdapter = CartAdapter(requireContext(), onMinusClick = {
            viewModel.setEvent(CartUIEvent.OnMinusCartCount(it))
        }, onPlusClick = {
            viewModel.setEvent(CartUIEvent.OnPlusCartCount(it))
        })

        state.cartList?.let {
            cartAdapter.submitList(state.cartList)
        }
        binding.apply {
            cartTotalPriceText.text = "${state.totalPrice}${resources.getText(R.string.priceIcon)}"
            state.cartList?.let { cartCompleteButton.changeVisibility(it.isNotEmpty()) }
            state.cartList?.let { cartTotalTitle.changeVisibility(it.isNotEmpty()) }
            state.cartList?.let { cartTotalPriceText.changeVisibility(it.isNotEmpty()) }
            cartEmptyText.changeVisibility(state.cartList.isNullOrEmpty())
            cartRecyclerview.adapter = cartAdapter
        }
    }

    override fun setupUI() {
        super.setupUI()
        viewModel.setEvent(CartUIEvent.OnGetAllCarts)
        binding.apply {
            cartRecyclerview.apply {
                layoutManager = LinearLayoutManager(requireContext())
            }

        }
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.cartCompleteButton.clickWithDebounce {
            viewModel.setEvent(CartUIEvent.OnCompleteCart)
        }
    }


}
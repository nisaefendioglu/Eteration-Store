package com.nisaefendioglu.eterationstore.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nisaefendioglu.eterationstore.R
import com.nisaefendioglu.eterationstore.core.BaseFragment
import com.nisaefendioglu.eterationstore.utils.changeVisibility
import com.nisaefendioglu.eterationstore.utils.clickWithDebounce
import com.nisaefendioglu.eterationstore.utils.hideKeyboard
import com.nisaefendioglu.eterationstore.databinding.FragmentHomeBinding
import com.nisaefendioglu.eterationstore.databinding.ProductLoadItemBinding
import com.nisaefendioglu.eterationstore.domain.models.Product
import com.nisaefendioglu.eterationstore.presentation.filter.FilterBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeUIEvent, HomeUIState, HomeUIEffect, HomeViewModel>(
        FragmentHomeBinding::inflate
    ) {

    override val viewModel: HomeViewModel by activityViewModels()

    override fun handleEffect(effect: HomeUIEffect) {
        when (effect) {
            is HomeUIEffect.ShowToast -> {
                Toast.makeText(requireContext(), "${effect.message} ${resources.getText(R.string.added)}", Toast.LENGTH_SHORT).show()
            }

            is HomeUIEffect.ShowSnackBar -> {
                val snackBar = Snackbar.make(binding.root, effect.message, Snackbar.LENGTH_SHORT)
                snackBar.show()
            }
        }
    }

    override fun setupUI() {
        super.setupUI()
        viewModel.setEvent(HomeUIEvent.OnGetAllProducts)
        val homeAdapter = HomeProductsAdapter(
            onItemClick = { product: Product ->
                val action =
                    HomeFragmentDirections.actionNavigationHomeFragmentToDetailFragment(product)
                findNavController().navigate(action)
            },
            onAddToCartClick = { product: Product ->
                viewModel.setEvent(HomeUIEvent.OnAddCartEntity(product))
            }
        )

        val recyclerView = binding.homeRecyclerview
        val gridLayoutManager = GridLayoutManager(activity, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = homeAdapter.getItemViewType(position)
                return if (viewType == HomeProductsAdapter.LOADING_VIEW_TYPE) 1
                else 2
            }
        }

        recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter =
                homeAdapter.withLoadStateFooter(footer = ProductLoadStateAdapter { homeAdapter.retry() })
            setHasFixedSize(true)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                homeAdapter.submitData(it.productList)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            homeAdapter.loadStateFlow.collectLatest { loadState ->
                when {
                    loadState.refresh is LoadState.Error -> {
                        viewModel.setEvent(HomeUIEvent.OnOpeningError)
                    }

                    loadState.append is LoadState.Error -> {
                        viewModel.setEvent(HomeUIEvent.OnSetLoadingError((loadState.append as LoadState.Error).error))
                    }

                    loadState.prepend is LoadState.Error -> {
                        viewModel.setEvent(HomeUIEvent.OnSetLoadingError((loadState.prepend as LoadState.Error).error))
                    }
                }
            }
        }
    }

    override fun observeState(state: HomeUIState) {
        binding.apply {
            homeProgress.changeVisibility(state.isLoading)
            homeRecyclerview.changeVisibility(!state.isLoading)
            homeTryAgainButton.apply {
                changeVisibility(state.isError)
                clickWithDebounce {
                    changeVisibility(false)
                    viewModel.setEvent(HomeUIEvent.OnTryAgain)
                }
            }
            homeSearchbar.apply {
                setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        viewModel.setEvent(HomeUIEvent.OnSetFilterText(v.text.toString()))
                        clearFocus()
                        hideKeyboard()
                        return@OnEditorActionListener true
                    }
                    false
                })
            }
            homeSelectFilterText.clickWithDebounce {
                val filterBottomSheet = FilterBottomSheetFragment()
                filterBottomSheet.show(parentFragmentManager, "FilterBottomSheet")

            }

        }

    }

    private inner class ProductLoadStateAdapter(private val retry: () -> Unit) :
        LoadStateAdapter<ProductLoadStateAdapter.LoadStateViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
            val binding = ProductLoadItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LoadStateViewHolder(binding)
        }

        override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
            holder.bind(loadState)
        }

        inner class LoadStateViewHolder(private val binding: ProductLoadItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

            init {
                binding.productLoadStateRetryText.setOnClickListener {
                    retry.invoke()
                }
            }

            fun bind(loadState: LoadState) {
                binding.productLoadStateProgress.isVisible = loadState is LoadState.Loading
                binding.productLoadStateRetryText.isVisible = loadState is LoadState.Error
            }
        }
    }
}

package com.nisaefendioglu.eterationstore.presentation.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nisaefendioglu.eterationstore.R
import com.nisaefendioglu.eterationstore.utils.clickWithDebounce
import com.nisaefendioglu.eterationstore.utils.hideKeyboard
import com.nisaefendioglu.eterationstore.databinding.FragmentFilterBottomSheetBinding
import com.nisaefendioglu.eterationstore.presentation.home.HomeUIEvent
import com.nisaefendioglu.eterationstore.presentation.home.HomeViewModel
import kotlinx.coroutines.launch

class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: HomeViewModel by activityViewModels()
    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var selectedBrands = emptyList<String>()
    private var selectedModels = emptyList<String>()
    private var selectedSortBy = R.id.filter_old_to_new

    private val brandAdapter = createFilterAdapter { selectedBrands = it }
    private val modelAdapter = createFilterAdapter { selectedModels = it }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        restoreState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureBottomSheetBehavior()
        setupRecyclerViews()
        setupListeners()
        observeState()
    }

    private fun restoreState() {
        selectedBrands = viewModel.uiState.value.brandFilterText
        selectedModels = viewModel.uiState.value.modelFilterText
        selectedSortBy = viewModel.uiState.value.sortFilterId
    }

    private fun configureBottomSheetBehavior() {
        val bottomSheetBehavior = BottomSheetBehavior.from(this.view?.parent as View)
        bottomSheetBehavior.isDraggable = false
    }

    private fun setupRecyclerViews() {
        brandAdapter.submitList(viewModel.uiState.value.brandsFilterList)
        modelAdapter.submitList(viewModel.uiState.value.modelsFilterList)

        binding.apply {
            filterBrandRecyclerview.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = brandAdapter
            }
            filterModelRecyclerview.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = modelAdapter
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            filterToolbar.setNavigationOnClickListener { dismiss() }

            filterRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                selectedSortBy = checkedId
            }

            setupSearchBar(filterBrandSearchbar, brandAdapter, viewModel.uiState.value.brandsFilterList)
            setupSearchBar(filterModelSearchbar, modelAdapter, viewModel.uiState.value.modelsFilterList)

            filterPrimaryButton.clickWithDebounce {
                applyFilters()
            }
        }
    }

    private fun setupSearchBar(searchBar: TextView, adapter: FilterAdapter, list: List<String>) {
        searchBar.apply {
            addTextChangedListener { editable ->
                filterList(adapter, editable.toString(), list)
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clearFocus()
                    hideKeyboard()
                    true
                } else false
            }
        }
    }

    private fun applyFilters() {
        viewModel.apply {
            setEvent(HomeUIEvent.OnSetModelFilter(selectedModels))
            setEvent(HomeUIEvent.OnSetBrandFilter(selectedBrands))
            setEvent(HomeUIEvent.OnSetSortFilterId(selectedSortBy))
            setEvent(HomeUIEvent.OnAddFilters)
        }
        dismiss()
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.filterRadioGroup.check(state.sortFilterId)
                brandAdapter.setSelectedList(state.brandFilterText)
                modelAdapter.setSelectedList(state.modelFilterText)
            }
        }
    }

    private fun filterList(adapter: FilterAdapter, text: String, list: List<String>) {
        adapter.submitList(if (text.isNotEmpty()) list.filter { it.contains(text) } else list)
    }

    private fun createFilterAdapter(onClick: (List<String>) -> Unit): FilterAdapter {
        return FilterAdapter(onClick)
    }

    override fun onStart() {
        super.onStart()
        configureFullScreenDialog()
    }

    private fun configureFullScreenDialog() {
        val bottomSheet =
            (dialog as BottomSheetDialog?)!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            BottomSheetBehavior.from(it).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
            }
            it.layoutParams = it.layoutParams.apply {
                height = WindowManager.LayoutParams.MATCH_PARENT
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

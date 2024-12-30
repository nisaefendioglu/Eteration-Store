package com.nisaefendioglu.eterationstore.presentation.home

import androidx.paging.PagingData
import com.nisaefendioglu.eterationstore.R
import com.nisaefendioglu.eterationstore.core.UIEffect
import com.nisaefendioglu.eterationstore.core.UIEvent
import com.nisaefendioglu.eterationstore.core.UIState
import com.nisaefendioglu.eterationstore.domain.models.Product
import com.nisaefendioglu.eterationstore.utils.Constants

data class HomeUIState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val productList: PagingData<Product> = PagingData.empty(),
    val searchFilterText: String = "",
    val sortFilterId: Int = R.id.filter_old_to_new,
    val brandFilterText: List<String> = emptyList(),
    val modelFilterText: List<String> = emptyList(),
    val brandsFilterList: List<String> = Constants.brandList,
    val modelsFilterList: List<String> = Constants.modelList
) :
    UIState

sealed class HomeUIEvent : UIEvent {
    data object OnGetAllProducts : HomeUIEvent()
    data object OnOpeningError : HomeUIEvent()
    data class OnSetLoadingError(val e: Throwable) : HomeUIEvent()
    data object OnTryAgain : HomeUIEvent()
    data class OnSetFilterText(val text: String) : HomeUIEvent()
    data class OnAddCartEntity(val product: Product) : HomeUIEvent()
    data class OnSetSortFilterId(val id: Int) : HomeUIEvent()
    data class OnSetBrandFilter(val brandList: List<String>) : HomeUIEvent()
    data class OnSetModelFilter(val modelList: List<String>) : HomeUIEvent()
    data object OnAddFilters : HomeUIEvent()



}

sealed class HomeUIEffect : UIEffect {
    data class ShowToast(val message: String) : HomeUIEffect()
    data class ShowSnackBar(val message: String) : HomeUIEffect()
}

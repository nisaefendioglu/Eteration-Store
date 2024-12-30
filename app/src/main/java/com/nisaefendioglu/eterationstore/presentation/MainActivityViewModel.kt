package com.nisaefendioglu.eterationstore.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nisaefendioglu.eterationstore.data.repository.FavoriteProductRepositoryImpl
import com.nisaefendioglu.eterationstore.domain.use_cases.FetchAllCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val fetchAllCartUseCase: FetchAllCartUseCase,
    private val favoriteProductRepositoryImpl: FavoriteProductRepositoryImpl
) : ViewModel() {

    private val _badgeCount = MutableStateFlow(0)
    val badgeCount = _badgeCount.asStateFlow()

    private val _favCount = MutableStateFlow(0)
    val favCount = _favCount.asStateFlow()

    init {
        fetchBadgeCount()
        fetchFavCount()
    }


    private fun fetchBadgeCount() {
        viewModelScope.launch {
            fetchAllCartUseCase().collect { cartEntities ->
                _badgeCount.value = cartEntities.size
            }
        }
    }


    private fun fetchFavCount() {
        viewModelScope.launch {
            favoriteProductRepositoryImpl.getAllFavorites().collect { favProduct ->
                _favCount.value = favProduct.size
            }
        }
    }
}

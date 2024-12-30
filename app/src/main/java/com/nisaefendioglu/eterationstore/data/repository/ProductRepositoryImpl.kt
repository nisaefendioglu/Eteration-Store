package com.nisaefendioglu.eterationstore.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.nisaefendioglu.eterationstore.data.remote.ApiService
import com.nisaefendioglu.eterationstore.data.remote.ProductPagingDataSource
import com.nisaefendioglu.eterationstore.domain.models.Product
import com.nisaefendioglu.eterationstore.domain.repository.ProductRepository

class ProductRepositoryImpl(private val apiService: ApiService) : ProductRepository {
    override suspend fun getProducts(
        filter: String,
        order: String,
        brand: String,
        model: String,
        sortedBy: String
    ): Pager<Int, Product> {
        return Pager(
            config = PagingConfig(
                pageSize = 4, // Define page size
                initialLoadSize = 4, // First load size
                enablePlaceholders = true, // Enable placeholders
                prefetchDistance = 1 // Distance before paging triggers
            ),
            pagingSourceFactory = {
                ProductPagingDataSource(
                    apiService = apiService,
                    filter = filter,
                    order = order,
                    brand = brand,
                    model = model,
                    sortedBy = sortedBy
                )
            }
        )
    }
}

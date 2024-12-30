package com.nisaefendioglu.eterationstore.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nisaefendioglu.eterationstore.domain.models.Product

class ProductPagingDataSource(
    private val apiService: ApiService,
    private val filter: String,
    private val order: String,
    private val brand: String,
    private val model: String,
    private val sortedBy: String
) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 1

        return try {
            val response = apiService.getProducts(
                page = page,
                limit = params.loadSize,
                filter = filter,
                order = order,
                brand = brand,
                model = model,
                sortedBy = sortedBy
            )

            val products = response.body().orEmpty()

            LoadResult.Page(
                data = products.map { it.toProduct() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (products.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let {
                it.prevKey?.plus(1) ?: it.nextKey?.minus(1)
            }
        }
    }
}

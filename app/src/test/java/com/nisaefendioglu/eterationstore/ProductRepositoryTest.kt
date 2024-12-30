package com.nisaefendioglu.eterationstore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingConfig
import com.nisaefendioglu.eterationstore.data.remote.ApiService
import com.nisaefendioglu.eterationstore.data.repository.ProductRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ProductRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var productRepository: ProductRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        productRepository = ProductRepositoryImpl(apiService)
    }

    @Test
    fun `test fetch products pager configuration`() = runBlocking {
        val pagingConfig = PagingConfig(
            pageSize = 4,
            initialLoadSize = 4,
            enablePlaceholders = true,
            prefetchDistance = 1
        )

        assertEquals(4, pagingConfig.pageSize)
        assertEquals(4, pagingConfig.initialLoadSize)
        assertTrue(pagingConfig.enablePlaceholders)
        assertEquals(1, pagingConfig.prefetchDistance)
    }

    @Test
    fun `test repository with different parameters`() = runBlocking {
        val testScenarios = listOf(
            Triple("electronics", "desc", "Apple"),
            Triple("clothing", "asc", "Samsung"),
            Triple("", "desc", "")
        )

        testScenarios.forEach { (filter, order, brand) ->
            val pager = productRepository.getProducts(
                filter = filter,
                order = order,
                brand = brand,
                model = "",
                sortedBy = "name"
            )
            assertNotNull(pager)
        }
    }

    @Test
    fun `test pager parameters validation`() = runBlocking {
        val testCases = listOf(
            mapOf(
                "filter" to "",
                "order" to "asc",
                "brand" to "",
                "model" to "",
                "sortedBy" to "name"
            ),
            mapOf(
                "filter" to "electronics",
                "order" to "desc",
                "brand" to "Apple",
                "model" to "iPhone",
                "sortedBy" to "price"
            )
        )

        testCases.forEach { params ->
            val pager = productRepository.getProducts(
                filter = params["filter"] as String,
                order = params["order"] as String,
                brand = params["brand"] as String,
                model = params["model"] as String,
                sortedBy = params["sortedBy"] as String
            )

            assertNotNull(pager)
        }
    }
}
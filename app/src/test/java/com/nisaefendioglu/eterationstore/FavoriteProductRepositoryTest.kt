package com.nisaefendioglu.eterationstore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nisaefendioglu.eterationstore.data.local.FavoriteProductDao
import com.nisaefendioglu.eterationstore.data.local.entity.FavoriteProduct
import com.nisaefendioglu.eterationstore.data.repository.FavoriteProductRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class FavoriteProductRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var favoriteProductDao: FavoriteProductDao

    private lateinit var favoriteProductRepository: FavoriteProductRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        favoriteProductRepository = FavoriteProductRepositoryImpl(favoriteProductDao)
    }

    @Test
    fun `test insert favorite product`() = runBlocking {
        val product = FavoriteProduct(
            id = 1,
            name = "Test Product",
            price = "100.0",
            productId = "1",
            image = "https://example.com/image.jpg"
        )

        favoriteProductRepository.insert(product)

        verify(favoriteProductDao).insert(product)
    }

    @Test
    fun `test delete favorite product`() = runBlocking {
        val productId = "1"
        favoriteProductRepository.delete(productId)
        verify(favoriteProductDao).delete(productId)
    }

    @Test
    fun `test get all favorites`() = runBlocking {
        val favoriteProducts = listOf(
            FavoriteProduct(
                id = 1,
                name = "Test Product",
                price = "100.0",
                productId = "1",
                image = "https://example.com/image.jpg"
            ),
            FavoriteProduct(
                id = 2,
                name = "Test Product 2",
                price = "200.0",
                productId = "2",
                image = "https://example.com/image2.jpg"
            )
        )

        `when`(favoriteProductDao.getAllFavorites()).thenReturn(flowOf(favoriteProducts))

        val result = favoriteProductRepository.getAllFavorites().first()

        assertEquals(2, result.size)
        assertEquals("Test Product", result[0].name)
        assertEquals("Test Product 2", result[1].name)
    }

    @Test
    fun `test is favorite`() = runBlocking {
        val product = FavoriteProduct(
            id = 1,
            name = "Test Product",
            price = "100.0",
            productId = "1",
            image = "https://example.com/image.jpg"
        )

        `when`(favoriteProductDao.isFavorite("1")).thenReturn(flowOf(product))

        val result = favoriteProductRepository.isFavorite("1").first()

        assertNotNull(result)
        assertEquals(1, result?.id)
        assertEquals("Test Product", result?.name)
    }

    @Test
    fun `test is not favorite`() = runBlocking {
        `when`(favoriteProductDao.isFavorite("999")).thenReturn(flowOf(null))

        val result = favoriteProductRepository.isFavorite("999").first()

        assertNull(result)
    }
}
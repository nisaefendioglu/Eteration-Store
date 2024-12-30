package com.nisaefendioglu.eterationstore

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nisaefendioglu.eterationstore.data.local.AppDatabase
import com.nisaefendioglu.eterationstore.data.local.FavoriteProductDao
import com.nisaefendioglu.eterationstore.data.local.entity.FavoriteProduct
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FavoriteProductDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var favoriteProductDao: FavoriteProductDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        favoriteProductDao = database.favoriteProductDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetFavoriteProduct() = runBlocking {
        val favoriteProduct = FavoriteProduct(
            id = 1,
            name = "Test Product",
            price = "100.0",
            productId = "1",
            image = "https://example.com/image.jpg"
        )

        favoriteProductDao.insert(favoriteProduct)

        val favorites = favoriteProductDao.getAllFavorites().first()

        assertEquals(1, favorites.size)
        assertEquals(favoriteProduct.productId, favorites[0].productId)
        assertEquals(favoriteProduct.name, favorites[0].name)
    }

    @Test
    fun deleteFavoriteProduct() = runBlocking {
        val favoriteProduct = FavoriteProduct(
            id = 1,
            name = "Test Product",
            price = "100.0",
            productId = "1",
            image = "https://example.com/image.jpg"
        )

        favoriteProductDao.insert(favoriteProduct)

        favoriteProductDao.delete("1")

        val favorites = favoriteProductDao.getAllFavorites().first()

        assertEquals(0, favorites.size)
    }

    @Test
    fun checkIsFavorite() = runBlocking {
        val favoriteProduct = FavoriteProduct(
            id = 1,
            name = "Test Product",
            price = "100.0",
            productId = "1",
            image = "https://example.com/image.jpg"
        )

        favoriteProductDao.insert(favoriteProduct)

        val isFavorite = favoriteProductDao.isFavorite("1").first()

        assertNotNull(isFavorite)
        assertEquals("1", isFavorite?.productId)
        assertEquals("Test Product", isFavorite?.name)
    }

    @Test
    fun checkIsNotFavorite() = runBlocking {
        val isFavorite = favoriteProductDao.isFavorite("999").first()

        assertNull(isFavorite)
    }

    @Test
    fun insertMultipleFavoriteProducts() = runBlocking {
        val favoriteProducts = listOf(
            FavoriteProduct(
                id = 1,
                name = "Product 1",
                price = "100.0",
                productId = "1",
                image = "https://example.com/image1.jpg"
            ),
            FavoriteProduct(
                id = 2,
                name = "Product 2",
                price = "200.0",
                productId = "2",
                image = "https://example.com/image2.jpg"
            )
        )

        favoriteProducts.forEach { favoriteProductDao.insert(it) }

        val favorites = favoriteProductDao.getAllFavorites().first()

        assertEquals(2, favorites.size)
        assertTrue(favorites.any { it.productId == "1" })
        assertTrue(favorites.any { it.productId == "2" })
    }

    @Test
    fun replaceExistingFavoriteProduct() = runBlocking {
        val initialProduct = FavoriteProduct(
            id = 1,
            name = "Initial Product",
            price = "100.0",
            productId = "1",
            image = "https://example.com/initial.jpg"
        )

        favoriteProductDao.insert(initialProduct)

        val replacementProduct = FavoriteProduct(
            id = 1,
            name = "Replaced Product",
            price = "200.0",
            productId = "1",
            image = "https://example.com/replaced.jpg"
        )

        favoriteProductDao.insert(replacementProduct)

        val favorites = favoriteProductDao.getAllFavorites().first()

        assertEquals(1, favorites.size)
        assertEquals("Replaced Product", favorites[0].name)
        assertEquals("200.0", favorites[0].price)
    }
}
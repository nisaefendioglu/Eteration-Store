package com.nisaefendioglu.eterationstore

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nisaefendioglu.eterationstore.data.local.AppDatabase
import com.nisaefendioglu.eterationstore.data.local.CartDao
import com.nisaefendioglu.eterationstore.data.local.entity.CartEntity
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
class CartDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var cartDao: CartDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        cartDao = database.cartDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetCartItem() = runBlocking {
        val cartItem = CartEntity(
            productId = 1,
            name = "Test Product",
            price = "100.0",
            count = 1
        )

        cartDao.insertCart(cartItem)

        val cartItems = cartDao.getAllCartEntities().first()

        assertEquals(1, cartItems.size)
        assertEquals(cartItem.productId, cartItems[0].productId)
        assertEquals(cartItem.name, cartItems[0].name)
    }

    @Test
    fun calculateTotalPrice() {
        val cartItem = CartEntity(
            productId = 1,
            name = "Test Product",
            price = "100.0",
            count = 2
        )

        val expectedTotalPrice = 200.0

        assertEquals(expectedTotalPrice, cartItem.totalPrice, 0.001)
    }

    @Test
    fun deleteAllCartEntities() = runBlocking {
        val cartItems = listOf(
            CartEntity(productId = 1, name = "Product 1", price = "100.0", count = 1),
            CartEntity(productId = 2, name = "Product 2", price = "200.0", count = 2)
        )

        cartItems.forEach { cartDao.insertCart(it) }

        val insertedItems = cartDao.getAllCartEntities().first()
        assertEquals(2, insertedItems.size)

        cartDao.deleteAllEntities()

        val remainingCartItems = cartDao.getAllCartEntities().first()

        assertEquals(0, remainingCartItems.size)
    }

    @Test
    fun getCartItemByProductId() = runBlocking {
        val cartItem = CartEntity(
            productId = 1,
            name = "Test Product",
            price = "100.0",
            count = 1
        )

        cartDao.insertCart(cartItem)

        val retrievedCartItem = cartDao.getCartItemByProductId(1)

        assertNotNull(retrievedCartItem)
        assertEquals(cartItem.name, retrievedCartItem?.name)
        assertEquals(cartItem.price, retrievedCartItem?.price)
        assertEquals(cartItem.count, retrievedCartItem?.count)
    }
}
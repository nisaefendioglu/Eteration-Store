package com.nisaefendioglu.eterationstore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nisaefendioglu.eterationstore.data.local.entity.CartEntity
import com.nisaefendioglu.eterationstore.data.local.entity.FavoriteProduct

@Database(entities = [CartEntity::class,FavoriteProduct::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun favoriteProductDao(): FavoriteProductDao
}
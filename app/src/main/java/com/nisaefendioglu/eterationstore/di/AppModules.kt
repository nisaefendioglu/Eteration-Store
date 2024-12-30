package com.nisaefendioglu.eterationstore.di

import android.content.Context
import androidx.room.Room
import com.nisaefendioglu.eterationstore.BuildConfig
import com.nisaefendioglu.eterationstore.data.local.AppDatabase
import com.nisaefendioglu.eterationstore.data.local.CartDao
import com.nisaefendioglu.eterationstore.data.local.FavoriteProductDao
import com.nisaefendioglu.eterationstore.data.remote.ApiService
import com.nisaefendioglu.eterationstore.data.repository.CartRepositoryImpl
import com.nisaefendioglu.eterationstore.data.repository.FavoriteProductRepositoryImpl
import com.nisaefendioglu.eterationstore.data.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideProductRepository(apiService: ApiService): ProductRepositoryImpl =
        ProductRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()

    @Provides
    @Singleton
    fun provideCartDao(db: AppDatabase): CartDao = db.cartDao()

    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao): CartRepositoryImpl =
        CartRepositoryImpl(cartDao)

    @Provides
    @Singleton
    fun provideFavoriteProductDao(db: AppDatabase): FavoriteProductDao =
        db.favoriteProductDao()

    @Provides
    @Singleton
    fun provideFavoriteProductRepository(favoriteProductDao: FavoriteProductDao): FavoriteProductRepositoryImpl =
        FavoriteProductRepositoryImpl(favoriteProductDao)
}

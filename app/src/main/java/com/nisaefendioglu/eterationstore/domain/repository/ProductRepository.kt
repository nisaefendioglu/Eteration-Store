package com.nisaefendioglu.eterationstore.domain.repository

import androidx.paging.Pager
import com.nisaefendioglu.eterationstore.domain.models.Product

interface ProductRepository {
    suspend fun getProducts(filter : String,order : String,brand : String,model : String,sortedBy : String) : Pager<Int, Product>
}
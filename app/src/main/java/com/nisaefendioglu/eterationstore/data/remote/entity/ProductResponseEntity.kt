package com.nisaefendioglu.eterationstore.data.remote.entity

import com.google.gson.annotations.SerializedName
import com.nisaefendioglu.eterationstore.domain.models.Product

data class ProductResponseEntity (
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("price") val price: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("model") val model: String? = null,
    @SerializedName("brand") val brand: String? = null,
    @SerializedName("id") val id: String? = null
) {
    fun toProduct(): Product = Product(
        createdAt = createdAt,
        name = name,
        image = image,
        price = price,
        description = description,
        model = model,
        brand = brand,
        id = id
    )
}

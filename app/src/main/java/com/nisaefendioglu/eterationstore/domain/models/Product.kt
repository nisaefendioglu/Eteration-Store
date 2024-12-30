package com.nisaefendioglu.eterationstore.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var createdAt: String? = null,
    var name: String? = null,
    var image: String? = null,
    var price: String? = null,
    var description: String? = null,
    var model: String? = null,
    var brand: String? = null,
    var id: String? = null,
    var isFav: Boolean = false
) : Parcelable
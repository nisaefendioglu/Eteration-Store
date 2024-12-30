package com.nisaefendioglu.eterationstore.presentation.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nisaefendioglu.eterationstore.R
import com.nisaefendioglu.eterationstore.databinding.ProductItemBinding
import com.nisaefendioglu.eterationstore.domain.models.Product
import com.nisaefendioglu.eterationstore.utils.clickWithDebounce
import com.nisaefendioglu.eterationstore.utils.getImageFromUrl

class HomeProductsAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onAddToCartClick: (Product) -> Unit
) : PagingDataAdapter<Product, HomeProductsAdapter.ProductViewHolder>(ProductDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                productItemPrice.text = "${product.price} ${itemView.context.getText(R.string.priceIcon)}"
                productItemName.text = product.brand
                productItemImage.getImageFromUrl(product.image)

                productItemButton.clickWithDebounce { onAddToCartClick(product) }
                root.clickWithDebounce { onItemClick(product) }
                productFavoriteButton?.isSelected = product.isFav
            }
        }
    }

    companion object {
        const val LOADING_VIEW_TYPE = 0
        private val ProductDiffCallback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
        }
    }
}

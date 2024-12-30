package com.nisaefendioglu.eterationstore.presentation.favorites


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nisaefendioglu.eterationstore.utils.clickWithDebounce
import com.nisaefendioglu.eterationstore.utils.getImageFromUrl
import com.nisaefendioglu.eterationstore.data.local.entity.FavoriteProduct
import com.nisaefendioglu.eterationstore.databinding.FavoriteItemBinding

class FavoriteProductAdapter(
    private val onDeleteClick: (FavoriteProduct) -> Unit
) : ListAdapter<FavoriteProduct, FavoriteProductAdapter.ViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteProduct: FavoriteProduct) {
            binding.apply {
                favoriteImage.getImageFromUrl(favoriteProduct.image)
                favoriteTitle.text = favoriteProduct.name
                favoritePrice.text = favoriteProduct.price
                favoriteDeleteIcon.clickWithDebounce { onDeleteClick(favoriteProduct) }
            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<FavoriteProduct>() {
        override fun areItemsTheSame(oldItem: FavoriteProduct, newItem: FavoriteProduct): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FavoriteProduct, newItem: FavoriteProduct): Boolean =
            oldItem == newItem
    }
}

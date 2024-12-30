package com.nisaefendioglu.eterationstore.presentation.filter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nisaefendioglu.eterationstore.utils.clickWithDebounce
import com.nisaefendioglu.eterationstore.databinding.FilterItemBinding

class FilterAdapter(private val onClick : (List<String>)->Unit
) : ListAdapter<String, FilterAdapter.FilterViewHolder>(CartDiffCallback()) {
    var selectedTextList = mutableListOf<String>()
    fun setSelectedList(selectedList: List<String>){
        selectedTextList = selectedList.toMutableList()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = FilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class FilterViewHolder(private val binding: FilterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item : String) {
            binding.filterItemCheckbox.apply {
                text = item
                isChecked = selectedTextList.contains(item)
                clickWithDebounce {
                    if (isChecked){
                        selectedTextList.add(item)
                    }else{
                        selectedTextList.remove(item)
                    }
                    onClick.invoke(selectedTextList.toList())
                }

            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
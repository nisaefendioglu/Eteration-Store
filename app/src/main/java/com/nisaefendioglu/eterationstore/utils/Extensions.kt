package com.nisaefendioglu.eterationstore.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * Adds a click listener to the view with a debounce mechanism.
 * Prevents multiple rapid clicks within the [debounceTime].
 */
fun View.clickWithDebounce(debounceTime: Long = 600L, action: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > debounceTime) {
            lastClickTime = currentTime
            action(it)
        }
    }
}

/**
 * Changes the visibility of a view.
 * If [isVisible] is true, sets the view to VISIBLE, otherwise GONE.
 */
fun View.changeVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

/**
 * Hides the soft keyboard associated with the view.
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Loads an image from the provided [url] into an ImageView using Glide.
 * Skips memory cache and uses disk caching strategy.
 */
fun ImageView.getImageFromUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

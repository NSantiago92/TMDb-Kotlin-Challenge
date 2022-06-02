package com.nsantiago.tmdbkotlinchallenge.common

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nsantiago.tmdbkotlinchallenge.R
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.repository.TMDbApiStatus
import com.nsantiago.tmdbkotlinchallenge.ui.MovieListAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Movie>?) {
    val adapter = recyclerView.adapter as MovieListAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun setImageUrl(imageView:ImageView, url: String?) {
    if (url.isNullOrEmpty()) return
    imageView.load(url) {
        placeholder(R.drawable.poster_placeholder)
        error(R.drawable.poster_placeholder)
    }
}

@BindingAdapter("apiStatus")
fun bindStatus(spinner: ProgressBar, status: TMDbApiStatus){
    when (status) {
        TMDbApiStatus.LOADING -> {
            spinner.visibility = View.VISIBLE
            spinner.updateLayoutParams<FrameLayout.LayoutParams> {
                gravity = Gravity.CENTER
            }
        }
        TMDbApiStatus.ERROR -> {
            spinner.visibility = View.GONE
        }
        TMDbApiStatus.DONE -> {
            spinner.visibility = View.GONE
        }
        TMDbApiStatus.REFRESHING -> {
            spinner.visibility = View.VISIBLE
            spinner.updateLayoutParams<FrameLayout.LayoutParams> {
                gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            }
        }
    }
}

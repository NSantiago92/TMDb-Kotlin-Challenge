package com.nsantiago.tmdbkotlinchallenge.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nsantiago.tmdbkotlinchallenge.databinding.FragmentMovieListBinding
import com.nsantiago.tmdbkotlinchallenge.databinding.MovieListItemBinding
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.repository.TMDbApiStatus
import com.nsantiago.tmdbkotlinchallenge.viewmodels.MovieListViewModel

class MovieListFragment : Fragment() {

    private val viewModel: MovieListViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, MovieListViewModel.Factory(activity.application))
            .get(MovieListViewModel::class.java)
    }

    private var movieListAdapter: MovieListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentMovieListBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        movieListAdapter = MovieListAdapter()
        binding.moviesRecyclerView.adapter = movieListAdapter


        binding.moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if( !recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN) && viewModel.apiStatus.value !== TMDbApiStatus.REFRESHING ) {
                    viewModel.loadNextPage()
                }
            }
        })

        viewModel.apiStatus.observe(viewLifecycleOwner) {
            when (it) {
                TMDbApiStatus.ERROR -> onNetworkError()
                TMDbApiStatus.DONE -> movieListAdapter!!.notifyDataSetChanged()
            }
        }
        return binding.root
    }


    private fun onNetworkError() {
        Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
    }
}


class MovieListAdapter : ListAdapter<Movie, MovieListAdapter.MovieListViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.title == newItem.title
        }
    }

    class MovieListViewHolder(
        private var binding: MovieListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movie = movie
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        return MovieListViewHolder(
            MovieListItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

}
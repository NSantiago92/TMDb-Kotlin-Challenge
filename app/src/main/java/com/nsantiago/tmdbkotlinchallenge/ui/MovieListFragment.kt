package com.nsantiago.tmdbkotlinchallenge.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.nsantiago.tmdbkotlinchallenge.R
import com.nsantiago.tmdbkotlinchallenge.databinding.FragmentMovieListBinding
import com.nsantiago.tmdbkotlinchallenge.viewmodels.MovieListViewModel

class MovieListFragment : Fragment() {

    private val viewModel: MovieListViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, MovieListViewModel.Factory(activity.application))
            .get(MovieListViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding =  FragmentMovieListBinding.inflate(layoutInflater)
        Log.d("BOKITA2", "Algo se creo")
        viewModel.movieList.observe(viewLifecycleOwner) {
            Log.d("BOKITA", it.toString())
        }
        return binding.root
    }


}
package com.nsantiago.tmdbkotlinchallenge.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.nsantiago.tmdbkotlinchallenge.R
import com.nsantiago.tmdbkotlinchallenge.databinding.FragmentMovieDetailBinding
import com.nsantiago.tmdbkotlinchallenge.viewmodels.MovieDetailViewModel
import com.nsantiago.tmdbkotlinchallenge.viewmodels.MovieListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.definition.Kind

class MovieDetailFragment : Fragment() {

    private var movieId = -1
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<MovieDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt("movieId")
        }
        viewModel.loadMovieDetail(movieId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        return binding.root
    }

}
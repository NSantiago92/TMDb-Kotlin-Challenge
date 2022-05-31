package com.nsantiago.tmdbkotlinchallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.nsantiago.tmdbkotlinchallenge.databinding.FragmentMovieDetailBinding
import com.nsantiago.tmdbkotlinchallenge.repository.TMDbApiStatus
import com.nsantiago.tmdbkotlinchallenge.viewmodels.MovieDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailFragment : Fragment() {

    private var _movieId = -1
    private var _movieTitle = ""
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<MovieDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _movieId = it.getInt("movieId")
            _movieTitle = it.getString("movieTitle")?:""
        }
        viewModel.loadMovieDetail(_movieId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = _movieTitle

        viewModel.apiStatus.observe(viewLifecycleOwner) {
            when(it) {
                TMDbApiStatus.ERROR -> binding.networkError.visibility = View.VISIBLE
                else -> binding.networkError.visibility = View.GONE
            }
        }
        return binding.root
    }

    fun onNavigateBack() {
        findNavController().navigateUp()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearMovieDetail()
    }

}
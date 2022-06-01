package com.nsantiago.tmdbkotlinchallenge.ui

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.nsantiago.tmdbkotlinchallenge.R
import com.nsantiago.tmdbkotlinchallenge.databinding.FragmentMovieDetailBinding
import com.nsantiago.tmdbkotlinchallenge.databinding.RateMovieDialogBinding
import com.nsantiago.tmdbkotlinchallenge.repository.TMDbApiStatus
import com.nsantiago.tmdbkotlinchallenge.viewmodels.MovieDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "MOVIE_DETAIL_FG"

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
            _movieTitle = it.getString("movieTitle") ?: ""
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
            when (it) {
                TMDbApiStatus.ERROR -> {
                    binding.networkError.visibility = View.VISIBLE
                    binding.rateBtn.visibility = View.GONE
                }
                else -> {
                    binding.networkError.visibility = View.GONE
                    binding.rateBtn.visibility = View.VISIBLE
                }
            }
        }
        binding.rateBtn.setOnClickListener { onMovieRateDialog() }
        return binding.root
    }

    private fun onMovieRateDialog() {
        var rating = 5.0f;
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        val inflater = requireActivity().layoutInflater;
        val dialogView = RateMovieDialogBinding.inflate(inflater)
        dialogView.rateSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                rating = (p1/5*5).div(10f).coerceAtLeast(0.5f)
                dialogView.rateText.text = rating.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                //progress started
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                //progress stopped
            }
        })

        builder
            ?.setTitle(R.string.rate_dialog_title)
            ?.setView(dialogView.root)
            ?.setPositiveButton(R.string.rate_dialog_positive_btn) { _, _ -> onRateMovie(rating) }
            ?.setCancelable(true)
        builder?.create()?.show()
    }

    private fun onRateMovie(rating: Float) {
        viewModel.rateMovie(_movieId, rating).observe(viewLifecycleOwner) {
            when (it) {
                true -> onRateMovieSuccess()
                false -> onRateMovieError()
            }
        }
    }

    private fun onRateMovieSuccess() {
        viewModel.refreshMovieDetail()
        Toast.makeText(activity, R.string.rate_movie_sucess_toast, Toast.LENGTH_LONG).show()
    }
    private fun onRateMovieError() = Toast.makeText(activity, R.string.rate_movie_error_toast, Toast.LENGTH_LONG).show()

    fun onNavigateBack() {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearMovieDetail()
    }

}
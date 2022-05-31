package com.nsantiago.tmdbkotlinchallenge.common

import com.nsantiago.tmdbkotlinchallenge.database.getDatabase
import com.nsantiago.tmdbkotlinchallenge.network.getTMDbService
import com.nsantiago.tmdbkotlinchallenge.repository.MoviesRepository
import com.nsantiago.tmdbkotlinchallenge.viewmodels.MovieDetailViewModel
import com.nsantiago.tmdbkotlinchallenge.viewmodels.MovieListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val serviceModule = module {
    single {
        getTMDbService()
    }
    single {
        getDatabase(androidContext())
    }
    single<MoviesRepository> {
        MoviesRepository(get(), get())
    }
    viewModel {
        MovieListViewModel(androidApplication(), get())
    }
    viewModel {
        MovieDetailViewModel(androidApplication(), get())
    }
}
package com.nsantiago.tmdbkotlinchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

///discover/movie?sort_by=popularity.desc
//https://api.themoviedb.org/discover/movie?sort_by=popularity.desc&api_key=b83c436ea678e7b94f949f696bc2f40d
//https://image.tmdb.org/t/p/w500/6JjfSchsU6daXk2AKX8EEBjO3Fm.jpg"
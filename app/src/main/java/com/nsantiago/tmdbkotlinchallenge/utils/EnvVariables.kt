package com.nsantiago.tmdbkotlinchallenge.utils

import io.github.cdimascio.dotenv.dotenv

object EnvVariables {
    private val dotenv = dotenv {
        directory = "./assets"
        filename = "env"
    }
    val API_KEY = dotenv["API_KEY"]
}

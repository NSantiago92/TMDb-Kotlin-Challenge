package com.nsantiago.tmdbkotlinchallenge.common

import io.github.cdimascio.dotenv.dotenv

object EnvVariables {
    private val dotenv = dotenv {
        directory = "./assets"
        filename = "env"
    }
    val API_KEY: String = dotenv["API_KEY"]
}

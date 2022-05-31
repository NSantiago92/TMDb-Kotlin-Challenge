package com.nsantiago.tmdbkotlinchallenge.utils

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(this.value)
}
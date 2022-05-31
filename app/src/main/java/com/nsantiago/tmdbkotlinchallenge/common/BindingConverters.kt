package com.nsantiago.tmdbkotlinchallenge.common

class BindingConverters {
    companion object {
        @JvmStatic
        fun floatToIntString(value: Float?): String? {
            return value?.toInt().toString()
        }
        @JvmStatic
        fun intToString(value: Int?): String? {
            return value?.toString()
        }
        @JvmStatic
        fun stringListToString(list: List<String>?): String? {
            return list?.joinToString(", ")
        }
    }
}
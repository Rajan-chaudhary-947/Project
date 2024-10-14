package com.Android.weatherApp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASEURL = "https://api.weatherapi.com"
    private fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
    val apiService : WeatherApi = getInstance().create(WeatherApi :: class.java)
}

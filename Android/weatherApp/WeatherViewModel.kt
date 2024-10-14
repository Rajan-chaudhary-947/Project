package com.example.adarshelectronics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adarshelectronics.api.NetworkResponse
import com.example.adarshelectronics.api.RetrofitInstance
import com.example.adarshelectronics.api.WeatherModel
import com.example.adarshelectronics.api.Constant
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.apiService
    private val _weatherData = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherData: LiveData<NetworkResponse<WeatherModel>> = _weatherData

    fun getData ( city : String) {
        _weatherData.value = NetworkResponse.Loading
        viewModelScope.launch {
            val response = weatherApi.getWeather(Constant.apiKey, city)

            try {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherData.value = NetworkResponse.Success(it)

                    }
                } else {
                    _weatherData.value = NetworkResponse.Error("Failed to fetch data")

                }
            } catch (e: Exception) {
                _weatherData.value = NetworkResponse.Error("Failed to fetch data")
            }

        }

    }

}
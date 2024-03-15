package weather.application.home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import weather.application.model.Repositry
import weather.application.model.WeatherResponse


class HomeViewModel(private var repo: Repositry) : ViewModel() {
//    private var weatherResponseMutableLiveData: WeatherResponse = WeatherResponse()
    lateinit var showWeatherResponse: WeatherResponse


    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getWeather(31.2554807, 29.9945374)
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    if (weatherResponse != null) {
                        showWeatherResponse=weatherResponse
                    }
                    Log.d("API", weatherResponse?.list?.size.toString())
                    Log.d("API", weatherResponse?.city.toString())

//                    fun saveCurrentDay(weatherResponse: WeatherResponse) {
//                        viewModelScope.launch(Dispatchers.IO) {
//                            repo.insert(weatherResponse)
//                        }
//                    }


                } else {
                    Log.d("API", "Response code: ${response.code()}")
                }
            } catch (e: Exception) { Log.d("API", "Error: ${e.message}", e)
            }
        }
    }
}
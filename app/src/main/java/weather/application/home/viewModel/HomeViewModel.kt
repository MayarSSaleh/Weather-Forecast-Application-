package weather.application.home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import weather.application.model.CustomedSetting
import weather.application.model.Repositry
import weather.application.model.WeatherResponse
// used by home fragment and setting fragment
class HomeViewModel(private var repo: Repositry) : ViewModel() {
    private val _weatherResponseLiveData = MutableLiveData<WeatherResponse>()
    val weatherResponseLiveData: LiveData<WeatherResponse> = _weatherResponseLiveData
    var customizedSetting:CustomedSetting= CustomedSetting()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {//check the customizedSetting to get the weather as required
                //
                //
                //
                //
                //
                //
                //
                //
                //

                val response = repo.getWeather(31.2554807, 29.9945374)
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    if (weatherResponse != null) {
                        _weatherResponseLiveData.postValue(weatherResponse)
//                        fun saveCurrentDay(weatherResponse: WeatherResponse) {
////                        viewModelScope.launch(Dispatchers.IO) {
////                            repo.insert(weatherResponse)
////                        }
////                    }
                    }
                } else {
                    Log.d("API", "Response code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.d("API", "Error: ${e.message}", e)
            }
        }
    }
}

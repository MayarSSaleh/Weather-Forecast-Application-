package weather.application.home.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import weather.application.MyConstant
import weather.application.MyConstant.SHARED_PREFS
import weather.application.model.Repositry
import weather.application.model.WeatherResponse
import java.util.Locale
import android.content.res.Configuration
import android.os.Build


// used by home fragment and setting fragment


class HomeViewModel(private val repo: Repositry) : ViewModel() {
    private lateinit var sharedPreferences: SharedPreferences
    private val _weatherResponseLiveData = MutableLiveData<WeatherResponse>()
     var language:String? = null
    val weatherResponseLiveData: LiveData<WeatherResponse> = _weatherResponseLiveData

    fun getWeather(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        sharedPreferences = context?.getSharedPreferences(SHARED_PREFS, 0)!!
        val units = when (sharedPreferences.getString(MyConstant.temp_unit, null)) {
            "Celsius" -> "metric"
            "Fahrenheit" -> "imperial"
            else -> null
        }
        when (sharedPreferences.getString(MyConstant.lan, null)){
            "Arabic" -> {
                language = "ar"
            }
            else -> null
        }

        try {
            val response = repo.getWeather(31.2554807, 29.9945374, units = units, lang = language)
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

    fun setLocale(languageCode: String, context: Context) {
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//        val configuration = Configuration()
//        configuration.locale = locale
//        val resources = context.resources
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            context.createConfigurationContext(configuration)
//        } else {
//            resources.updateConfiguration(configuration, resources.displayMetrics)
//        }
//        Log.d("important", "Language changed to: $languageCode")
    }
}

package weather.application.home.viewModel

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import weather.application.MainActivity
import weather.application.MyConstant
import weather.application.MyConstant.SHARED_PREFS
import weather.application.model.Repositry
import weather.application.model.WeatherResponse
import weather.application.setting.SettingFragment
import java.util.Locale


// used by home fragment and setting fragment

class HomeViewModel(private val repo: Repositry) : ViewModel() {
    private lateinit var sharedPreferences: SharedPreferences
    private val _weatherResponseLiveData = MutableLiveData<WeatherResponse>()
    var language: String? = null
    val weatherResponseLiveData: LiveData<WeatherResponse> = _weatherResponseLiveData
    lateinit var editor: SharedPreferences.Editor


    fun getWeather(context: Context, longitude: Double, latitude: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            sharedPreferences = context?.getSharedPreferences(SHARED_PREFS, 0)!!
            val units = when (sharedPreferences.getString(MyConstant.temp_unit, null)) {
                "Celsius" -> "metric"
                "Fahrenheit" -> "imperial"
                else -> null
            }
            when (sharedPreferences.getString(MyConstant.lan, null)) {
                "ar" -> {
                    language = "ar"
                }

                else -> null
            }

            try {
                val response = repo.getWeather(longitude, latitude, units = units, lang = language)
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

   fun mySetLocale(languageCode: String, context: Context?, activity: Activity?) {
       sharedPreferences = context?.getSharedPreferences(SHARED_PREFS, 0)!!

       if (context != null && activity != null) {
           val locale = Locale(languageCode)
           Locale.setDefault(locale)
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               updateResources(context, languageCode)
           } else {
               updateResourcesLegacy(context, languageCode)
           }
           sharedPreferences.getString(MyConstant.lan, "en")
           editor = sharedPreferences.edit()
           editor.putString(MyConstant.curentLanguage, languageCode)
           editor.apply()

           restartToChangeAppLanguage(activity, context)
       }
   }


   private fun updateResourcesLegacy(context: Context, language: String): Context? {
       val locale = Locale(language)
       Locale.setDefault(locale)
       val resources = context.resources
       val configuration = resources.configuration
       configuration.locale = locale
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
           configuration.setLayoutDirection(locale)
       }
       resources.updateConfiguration(configuration, resources.displayMetrics)
       return context
   }

   @TargetApi(Build.VERSION_CODES.N)
   private fun updateResources(context: Context?, language: String): Context? {
       val locale = Locale(language)
       Locale.setDefault(locale)
       val config = Configuration()
       val configuration = context?.resources?.configuration
       configuration?.setLocale(locale)
       config.setLocale(locale)
       configuration?.setLayoutDirection(locale)
       context?.resources?.updateConfiguration(
           config,
           context?.resources?.displayMetrics
       )

       if (configuration != null) {
           return context?.createConfigurationContext(configuration)
       } else {
           return null
       }
   }

   private fun restartToChangeAppLanguage(activity: Activity,context: Context) {
       activity.finishAffinity()
       startSplash(context)
   }

    fun startSplash(context: Context?) {
        context?.let {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}

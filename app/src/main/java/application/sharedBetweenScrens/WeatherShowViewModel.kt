package application.sharedBetweenScrens

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import application.MyConstant
import application.MyConstant.SHARED_PREFS
import application.application.MainActivity
import application.model.APiStateOrLocalStateFromLastWeather
import application.model.InterfaceRepository
import application.model.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import java.util.Locale


// used by home fragment and setting fragment

class WeatherShowViewModel(private val repo: InterfaceRepository) : ViewModel() {

    private lateinit var sharedPreferences: SharedPreferences
    private val _weatherResponseLiveData: MutableStateFlow<APiStateOrLocalStateFromLastWeather> =
        MutableStateFlow(APiStateOrLocalStateFromLastWeather.Loading)
    val weatherResponseLiveData: StateFlow<APiStateOrLocalStateFromLastWeather> = _weatherResponseLiveData
    var language: String? = null
    lateinit var editor: SharedPreferences.Editor

    fun getWeather(context: Context, longitude: Double, latitude: Double) {

        Log.d("Wetaherreeeee",":   " + longitude + "  " + latitude)
        // check the internet to
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

        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getWeather(longitude, latitude, units = units, lang = language)
            response.catch { e ->
                _weatherResponseLiveData.value = APiStateOrLocalStateFromLastWeather.Failure(e)
            }.collect {
                _weatherResponseLiveData.value = APiStateOrLocalStateFromLastWeather.Success(it!!)
                updateCurrentWeather(it)
            }
        }
    }

    fun getLastWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getLastWeather()
            response.catch { e ->
                _weatherResponseLiveData.value = APiStateOrLocalStateFromLastWeather.Failure(e)
            }.collect {
                if(it!=null){
                _weatherResponseLiveData.value = APiStateOrLocalStateFromLastWeather.Success(it!!)
            }}
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

    private fun updateCurrentWeather(weatherResponse: WeatherResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteLocation()
            repo.insertWeather(weatherResponse)
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

    private fun restartToChangeAppLanguage(activity: Activity, context: Context) {
        activity.finishAffinity()
        startSplash(context)
    }

    fun startSplash(context: Context?) {
        context?.let {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo?
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

}
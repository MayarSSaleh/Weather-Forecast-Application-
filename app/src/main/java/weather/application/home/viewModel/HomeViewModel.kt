package weather.application.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import weather.application.model.Repositry


class HomeViewModel(private var repo: Repositry) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                val response = repo.getWeather(37.7749, -122.4194)
                val response = repo.getWeather(31.2554807, 29.9945374)

                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    Log.d("API", weatherResponse?.list?.size.toString())
                    Log.d("API", weatherResponse?.city.toString())
                        for ( i in weatherResponse?.list!!) {
                            Log.d("API",i.toString())
                    }

                } else {
                    // Handle error response
                    Log.d("API", "Response code: ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle network or parsing errors
                Log.d("API", "Error: ${e.message}", e)
            }
        }
    }
}
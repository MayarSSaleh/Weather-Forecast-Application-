package application.ShowWeatherDeailrsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import application.model.InterfaceRepository


@Suppress("UNCHECKED_CAST")
class WeatherShowModelFactory (private val repo: InterfaceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherShowViewModel::class.java)) {
            WeatherShowViewModel(repo) as T
        } else {
            throw IllegalArgumentException("viewModel class not found")
        }
    }
}

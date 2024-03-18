package weather.application.fav.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import weather.application.model.Repositry


class FavViewModelFactory(private val repo: Repositry) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
            FavViewModel(repo) as T
        } else {
            throw IllegalArgumentException("viewModel class not found")
        }
    }
}
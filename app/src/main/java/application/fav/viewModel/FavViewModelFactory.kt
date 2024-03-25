package application.fav.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import application.model.InterfaceRepository
import application.model.Repository

@Suppress("UNCHECKED_CAST")
class FavViewModelFactory(private val repo: InterfaceRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
            FavViewModel(repo) as T
        } else {
            throw IllegalArgumentException("viewModel class not found")
        }
    }
}
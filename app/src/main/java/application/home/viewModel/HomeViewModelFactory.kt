package application.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import application.model.InterfaceRepository
import application.model.Repository


@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory (private val repo: InterfaceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repo) as T
        } else {
            throw IllegalArgumentException("viewModel class not found")
        }
    }
}

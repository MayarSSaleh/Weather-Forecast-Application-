package application.alerts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import application.model.InterfaceRepository

@Suppress("UNCHECKED_CAST")
class AlertViewModelFactory(private val repo: InterfaceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            AlertViewModel(repo) as T
        } else {
            throw IllegalArgumentException("viewModel class not found")
        }
    }
}
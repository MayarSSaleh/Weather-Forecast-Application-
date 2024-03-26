package application.alerts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import application.model.InterfaceRepository
import application.model.LocalStateFavouirteLocations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel(private var repo: InterfaceRepository) : ViewModel() {

    private val _aletsList: MutableStateFlow<LocalStateFavouirteLocations> =
        MutableStateFlow(LocalStateFavouirteLocations.LoadingLocal)
    val aletsList: StateFlow<LocalStateFavouirteLocations> = _aletsList


    init {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllAlerts()
            response.catch { e ->
                _aletsList.value = LocalStateFavouirteLocations.FailureLocal(e)
            }.collect {
                _aletsList.value = LocalStateFavouirteLocations.SuccessLocal(it!!)
            }
        }


    }
}
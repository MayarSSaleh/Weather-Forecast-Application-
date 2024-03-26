package application.alerts.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import application.model.Alert
import application.model.InterfaceRepository
import application.model.LocalStateAlerts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel(private var repo: InterfaceRepository) : ViewModel() {

    private val _alertsList: MutableStateFlow<LocalStateAlerts> =
        MutableStateFlow(LocalStateAlerts.LoadingLocaAlertl)
    val alertsList: StateFlow<LocalStateAlerts> = _alertsList

    init {
        getAlerts()
    }

    private fun getAlerts() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllAlerts()
            response.catch { e ->
                _alertsList.value = LocalStateAlerts.FailureLocalAlert(e)
            }.collect {
                _alertsList.value = LocalStateAlerts.SuccessLocalAlert(it!!)
            }
        }
    }

    fun insertALert(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("alert", "$alert")
            repo.insertAlert(alert)
            getAlerts()
        }
    }
    fun deleteALL(){
//        repo.deleteAllAlerts()
    }
}

package application.fav.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import application.model.FavLocation
import application.model.LocalStateFavouirteLocations
import application.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavViewModel(private var repo: Repository) : ViewModel() {

    private val _favLocations: MutableStateFlow<LocalStateFavouirteLocations> =
        MutableStateFlow(LocalStateFavouirteLocations.LoadingLocal)
    val favLocations: StateFlow<LocalStateFavouirteLocations> = _favLocations

    init {
        getFavLocations()
    }

    private fun getFavLocations() {//Preventing UI Thread Blocking
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllLocalLocation()
            response.catch { e ->
                _favLocations.value = LocalStateFavouirteLocations.FailureLocal(e)
            }.collect {
                _favLocations.value = LocalStateFavouirteLocations.SuccessLocal(it!!)
            }
        }
    }

    fun insertFavLocation(favLocation: FavLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(favLocation)
            getFavLocations()
        }
    }

    fun deleteFavLocation(favLocation: FavLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFavLocation(favLocation)
            getFavLocations()
        }
    }

}
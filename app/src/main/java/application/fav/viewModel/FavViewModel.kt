package application.fav.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import application.model.FavLocation
import application.model.InterfaceRepository
import application.model.LocalStateFavouriteLocations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavViewModel(private var repo: InterfaceRepository) : ViewModel() {

    private val _favLocations: MutableStateFlow<LocalStateFavouriteLocations> =
        MutableStateFlow(LocalStateFavouriteLocations.LoadingLocal)

    val favLocations: StateFlow<LocalStateFavouriteLocations> = _favLocations

    fun getFavLocations() {
        //Preventing UI Thread Blocking
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllFavLocation()
            response.catch { e ->
                _favLocations.value = LocalStateFavouriteLocations.FailureLocal(e)
            }.collect {
                _favLocations.value = LocalStateFavouriteLocations.SuccessLocal(it!!)
            }
        }
    }

    init {
        getFavLocations()
    }
    fun deleteFavLocation(favLocation: FavLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFavLocation(favLocation)
            getFavLocations()
        }
    }


    fun insertFavLocation(favLocation: FavLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(favLocation)
            getFavLocations()
        }
    }
}
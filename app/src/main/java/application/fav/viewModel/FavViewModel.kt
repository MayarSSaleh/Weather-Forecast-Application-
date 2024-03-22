package application.fav.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import application.model.FavLocation
import application.model.LocalStateFavouirteLocations
import application.model.Repositry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavViewModel(private var repo: Repositry) : ViewModel() {
//    private var _favLocations: MutableLiveData<List<FavLocation>> = MutableLiveData<List<FavLocation>>()
//    val favLocations: LiveData<List<FavLocation>> = _favLocations

    private val _favLocations: MutableStateFlow<LocalStateFavouirteLocations> =
        MutableStateFlow(LocalStateFavouirteLocations.LoadingLocal)

    val favLocations: StateFlow<LocalStateFavouirteLocations> = _favLocations

    init {
        getFavLocations()
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

    private fun getFavLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllLocalLocation()
            response.catch { e ->
                _favLocations.value = LocalStateFavouirteLocations.FailureLocal(e)
            }.collect {
                _favLocations.value = LocalStateFavouirteLocations.SuccessLocal(it!!)
            }
        }

//        viewModelScope.launch(Dispatchers.IO) {
//            val response = repo.getWeather(longitude, latitude, units = units, lang = language)
//            response.catch { e ->
//                _weatherResponseLiveData.value = APiState.Failure(e)
//            }.collect {
//                _weatherResponseLiveData.value = APiState.Success(it!!)
//                updateCurrentWeather(it)
//            }
//        }
    }
}

//        private fun getFavLocations() {
//            viewModelScope.launch(Dispatchers.IO) {
//                repo.getAllLocalLocation().collect {
//                    _favLocations.postValue(it)
//                }
//            }
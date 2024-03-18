package weather.application.fav.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import weather.application.model.FavLocation
import weather.application.model.Repositry

class FavViewModel(private var repo: Repositry) : ViewModel() {
    private var _favLocations: MutableLiveData<List<FavLocation>> = MutableLiveData<List<FavLocation>>()
    val favLocations: LiveData<List<FavLocation>> = _favLocations

    init {
        getFavLocations()
    }

//    fun insertFavLocation(favLocation: FavLocation) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repo.insert(favLocation)
//            getFavLocations()
//        }
//    }

    fun deleteFavLocation(favLocation: FavLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFavLocation(favLocation)
            getFavLocations()
        }
    }

    private fun getFavLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllLocalLocation().collect() { myLocations ->
                _favLocations.postValue(myLocations)
            }
        }
    }
}
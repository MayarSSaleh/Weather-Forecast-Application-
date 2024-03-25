package application.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class FakeRepository : InterfaceRepository {
    var favLocation =FavLocation(("testGet"),0.0,0.0)
    private var weatherResponsList: MutableList<WeatherResponse>? = mutableListOf()
    private var repoFavLocationsList: List<FavLocation> = mutableListOf(favLocation)

    val _favLocations: MutableStateFlow<LocalStateFavouirteLocations> =
        MutableStateFlow(LocalStateFavouirteLocations.LoadingLocal)

    override fun getAllFavLocation(): Flow<List<FavLocation>> {
        return flow {

            val favLocationsList =
                when (val value = _favLocations.value) {
                    is LocalStateFavouirteLocations.LoadingLocal -> {
                        // Update state to SuccessLocal after delay
                        _favLocations.value = LocalStateFavouirteLocations.LoadingLocal
                    }
                    is LocalStateFavouirteLocations.SuccessLocal -> {
                        _favLocations.value = LocalStateFavouirteLocations.SuccessLocal(repoFavLocationsList)
                    }
                    else -> emptyList<FavLocation>()
                }
            emit(favLocationsList as List<FavLocation>)
        }
    }


    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        units: String?,
        lang: String?
    ): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }


    override suspend fun deleteFavLocation(favLocation: FavLocation) {
//        favLocations?.remove(favLocation)
    }

    override suspend fun insert(favLocation: FavLocation) {
//        favLocations?.add(favLocation)
    }

    override suspend fun getLastWeather(): Flow<WeatherResponse> {
        return flow {
            // Create a copy of the list to avoid exposing the mutable reference
            val weatherListCopy = weatherResponsList?.toList() ?: emptyList()
            emitAll(weatherListCopy.asFlow())
        }
    }

    override suspend fun deleteLocation() {
        TODO("Not yet implemented")
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        TODO("Not yet implemented")
    }
}
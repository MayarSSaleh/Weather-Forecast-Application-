package application.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository : InterfaceRepository {
    var favLocation = FavLocation(("testGet"), 0.0, 0.0)
    private var weatherResponseLeastSaved: Flow<WeatherResponse>? = null
    private var weatherResponseFromApi: WeatherResponse = WeatherResponse(emptyList(), City("Alex"))
    private var repoFavLocationsList: List<FavLocation> = mutableListOf()

    val _favLocations: MutableStateFlow<LocalStateFavouirteLocations> =
        MutableStateFlow(LocalStateFavouirteLocations.LoadingLocal)

    override fun getAllFavLocation(): Flow<List<FavLocation>> {
        return flow {
            repoFavLocationsList
        }
    }


    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        units: String?,
        lang: String?
    ): Flow<WeatherResponse> {
        return flow {
            weatherResponseFromApi
        }
    }


    override suspend fun deleteFavLocation(favLocation: FavLocation) {
//        favLocations?.remove(favLocation)
    }

    override suspend fun insert(favLocation: FavLocation) {
//        favLocations?.add(favLocation)
    }

    override suspend fun getLastWeather(): Flow<WeatherResponse> {
        return flow {
            weatherResponseLeastSaved
        }
    }

    override suspend fun deleteLocation() {
        weatherResponseLeastSaved=null
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        TODO("Not yet implemented")
    }
}
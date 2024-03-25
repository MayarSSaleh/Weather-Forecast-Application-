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

    private var weatherResponseLeastSaved: WeatherResponse? = null
    private var repoFavLocationsList: MutableList<FavLocation>? =null

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
            WeatherResponse(emptyList(), City("Alex"))
        }
    }

    override suspend fun deleteFavLocation(favLocation: FavLocation) {
        repoFavLocationsList?.remove(favLocation)
    }

    override suspend fun insert(favLocation: FavLocation) {
        repoFavLocationsList?.add(favLocation)
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
        weatherResponseLeastSaved =weatherResponse
    }
}
package application.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository : InterfaceRepository {

    private var weatherResponseLeastSaved: WeatherResponse? = null
    private var repoFavLocationsList: MutableList<FavLocation> = mutableListOf()
    private var repoAlertLocationsList: MutableList<Alert> = mutableListOf()


    override fun getAllFavLocation(): Flow<List<FavLocation>> = flow {
        emit(repoFavLocationsList)
    }

    override suspend fun deleteFavLocation(favLocation: FavLocation) {
        repoFavLocationsList.remove(favLocation)
    }

    override suspend fun insert(favLocation: FavLocation) {
        repoFavLocationsList.add(favLocation)
    }

    override suspend fun deleteAlert(alert: Alert) {
        repoAlertLocationsList.remove(alert)
    }

    override fun getAllAlerts(): Flow<List<Alert>>  = flow {
        emit(repoAlertLocationsList)
    }

    override suspend fun insertAlert(alert: Alert) {
        repoAlertLocationsList.add(alert)
    }

    override suspend fun deleteALLNotification() {
        val iterator = repoAlertLocationsList.iterator()
        while (iterator.hasNext()) {
            val alert = iterator.next()
            if (alert.typeOfAlarm == "Notification") {
                iterator.remove()
            }
        }
    }

    override suspend fun deleteALLAlarms() {
        val iterator = repoAlertLocationsList.iterator()
        while (iterator.hasNext()) {
            val alert = iterator.next()
            if (alert.typeOfAlarm == "Alarm") {
                iterator.remove()
            }
        }
    }

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        units: String?,
        lang: String?
    ): Flow<WeatherResponse> {
        return flowOf(
            WeatherResponse(emptyList(), City("Alex"))
        )
    }


    override suspend fun getLastWeather(): Flow<WeatherResponse> {
        return flow {
            WeatherResponse(emptyList(), City("Alex"))
        }
    }

    override suspend fun deleteLocation() {
        weatherResponseLeastSaved = null
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        weatherResponseLeastSaved = weatherResponse
    }


}
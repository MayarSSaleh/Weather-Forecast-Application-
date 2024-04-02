package application.data.network

import application.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class RemoteDataSource (private val weatherService: WeatherService ) : InterfaceRemoteDataSource {

    override suspend fun getWeather(latitude: Double, longitude: Double, units: String?, lang: String?): Flow<WeatherResponse> {
        return flow {
            emit(weatherService.getWeatherDetails(latitude, longitude, units = units, lang = lang))
        }
    }
}
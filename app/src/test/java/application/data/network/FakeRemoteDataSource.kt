package application.data.network

import application.model.City
import application.model.WeatherItem
import application.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteDataSource : InterfaceRemoteDataSource {
    val list: List<WeatherItem> = emptyList()
    val city = City("Alex")


    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        units: String?,
        lang: String?
    ): Flow<WeatherResponse> {
        var weatherResponse=WeatherResponse(list, city)
        return flow { emit(weatherResponse) }
    }
}
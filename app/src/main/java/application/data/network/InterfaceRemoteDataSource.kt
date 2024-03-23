package application.data.network

import application.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface InterfaceRemoteDataSource {

    suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        units: String?,
        lang: String?
    ): Flow<WeatherResponse>
}
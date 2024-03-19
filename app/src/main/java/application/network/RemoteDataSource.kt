package application.network

import application.model.WeatherResponse
import retrofit2.Response


class RemoteDataSource {
    private val weatherService: WeatherService by lazy {
        RetrofitHelper.retrofit.create(WeatherService::class.java)
    }
    suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        units: String?,
        lang: String?
    ): Response<WeatherResponse> {
        return weatherService.getWeatherDetails(latitude, longitude, units = units, lang = lang)
    }

}
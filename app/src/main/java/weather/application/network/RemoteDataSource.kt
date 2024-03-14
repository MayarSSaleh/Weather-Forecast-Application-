package weather.application.network

import retrofit2.Response
import weather.application.model.WeatherResponse


class RemoteDataSource {
    private val weatherService: WeatherService by lazy {
        RetrofitHelper.retrofit.create(WeatherService::class.java)
    }

    suspend fun getWeather(latitude:Double , longitude:Double) : Response<WeatherResponse> {
        return weatherService.getWeatherDetails(latitude,longitude)
    }

}
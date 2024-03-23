package application.data.network

import application.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    companion object {
        const val API_KEY = "7a6155686334997733d302ec7ac0544b"
    }

    @GET("forecast?")
    suspend fun getWeatherDetails(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = API_KEY,
        @Query("units") units: String? = null,
        @Query("lang") lang: String? = null
    ): WeatherResponse

}

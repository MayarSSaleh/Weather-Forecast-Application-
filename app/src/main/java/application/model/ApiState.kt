package application.model

sealed class APiState {
    data class Success(val data: WeatherResponse) : APiState()
    data class Failure(val msg: Throwable) : APiState()
    object Loading : APiState()
}
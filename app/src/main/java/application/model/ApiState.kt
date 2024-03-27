package application.model

sealed class APiStateOrLocalStateFromLastWeather {
    data class Success(val data: WeatherResponse) : APiStateOrLocalStateFromLastWeather()
    data class Failure(val msg: Throwable) : APiStateOrLocalStateFromLastWeather()
    object Loading : APiStateOrLocalStateFromLastWeather()
}

sealed class LocalStateFavouirteLocations {
    data class SuccessLocal(val data: List<FavLocation>) : LocalStateFavouirteLocations()
    data class FailureLocal(val msg: Throwable) : LocalStateFavouirteLocations()
    object LoadingLocal : LocalStateFavouirteLocations()
}

sealed class LocalStateAlerts {
    data class SuccessLocalAlert(val data: List<Alert>) : LocalStateAlerts()
    data class FailureLocalAlert(val msg: Throwable) : LocalStateAlerts()
    object LoadingLocaAlertl : LocalStateAlerts()
}

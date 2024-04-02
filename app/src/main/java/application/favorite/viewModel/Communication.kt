package application.favorite.viewModel

import application.model.FavLocation

interface Communication {
    // use it to open a fav location from fav fragment and open it in home fragment+ parsing object, that happend by main activity implmenetation
    fun setFaLocationAtHome(favLocation: FavLocation)
}
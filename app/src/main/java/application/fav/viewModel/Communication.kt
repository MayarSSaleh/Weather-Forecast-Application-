package application.fav.viewModel

import application.model.FavLocation

interface Communication {
    fun setFavLocaionAtHome(favLocation: FavLocation)
}
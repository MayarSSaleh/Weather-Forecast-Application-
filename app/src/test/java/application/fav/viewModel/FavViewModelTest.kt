package application.fav.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import application.model.FavLocation
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    fun setUp() {
        var favLocation: FavLocation
    }

    fun tearDown() {
    }

    @Test
    fun getFavLocations_AddOne_RetrunOn() {

    }

    @Test
    fun getFavLocations_noFav_RetrunZero() {
        //given
//        repo.getAllLocalLocation()
//        //        when observe on favLocations flow
//        var result = vieModel.favLocations.value
//
//        assertThat(result, notNullValue())

    }

    @Test
    fun insertFavLocation(favLocation: FavLocation) {

    }

    @Test
    fun deleteFavLocation(favLocation: FavLocation) {
    }
}
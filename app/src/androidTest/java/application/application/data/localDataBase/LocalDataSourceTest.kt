package application.application.data.localDataBase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import application.data.localDataBase.AppDataBase
import application.data.localDataBase.FavLocationsDao
import application.data.localDataBase.LocalDataSource
import application.model.City
import application.model.FavLocation
import application.model.WeatherResponse
import junit.framework.TestCase
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LocalDataSourceTest {
    lateinit var database: AppDataBase
    lateinit var localDataSource: LocalDataSource

    @get:Rule
    val rule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        //to avoid change the real data
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDataBase::class.java
        ).build()
        localDataSource = LocalDataSource(getApplicationContext())
    }

    @After
    fun finish() {
        database.close()
    }


    @Test
    fun getFavLocations_getEmptyList() = runTest {
        //when
        val getResult = localDataSource.getAllFavLocations()
        val result = getResult.first()
        //assert
        assertNotNull(result)
        assertThat(result, `is`(emptyList<FavLocation>()))
    }

    @Test
    fun getTheInserted_insertFav_getHisDetails() = runTest {
        // Given
        val favLocations = FavLocation("alex", 0.0, 0.0)
        localDataSource.insertFavLocation(favLocations)
        //when
        val getResult = localDataSource.getAllFavLocations()
        val result = getResult.first()
        //assert
        assertNotNull(result)
        assertThat(result.size, `is`(1))
        assertThat(result.get(0).locationName, `is`("alex"))
        assertThat(result.get(0).latitude, `is`(0.0))
        assertThat(result.get(0).longitude, `is`(0.0))
    }

    @Test
    fun deleteTheInsertedFavLocation_insertFav_getEmptyList() = runTest {
        // Given
        val favLocations = FavLocation("alex", 0.0, 0.0)
        localDataSource.insertFavLocation(favLocations)
        localDataSource.deleteFavLocation(favLocations)
        //when
        val getResult = localDataSource.getAllFavLocations()
        val result = getResult.first()
        //assert
        assertNotNull(result)
        assertThat(result, `is`(emptyList()))
    }

    @Test
    fun getLeastWeatherResponse_getDefaultNorthernState() = runTest {
        //when
        val getResult = localDataSource.getLestWeathear()
        val result = getResult.first()
        //assert
        assertNotNull(result)
        assertThat(result.city, `is`("Northern State"))
    }

    @Test
    fun getTheInsertedWeather_insertWeather_getHisDetails() = runTest {
        // Given
        localDataSource.insertWeather(WeatherResponse(emptyList(), City("Alexandria")))
        //when
        val getResult = localDataSource.getLestWeathear()
        val result = getResult.first()
        //assert
        assertNotNull(result)
        assertThat(result.city, `is`("Alexandria"))
        assertThat(result.list, `is`(emptyList()))
    }
}
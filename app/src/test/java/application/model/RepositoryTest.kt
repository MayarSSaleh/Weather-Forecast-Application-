package application.model

import application.data.localDataBase.FakeLocalDataSource
import application.data.network.FakeRemoteDataSource
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


// 5  some local and some remote


class RepositoryTest {

    val list: List<WeatherItem> = emptyList()
    private val lastResponse = WeatherResponse(list, City("Alex"))

    private val favLocation = FavLocation("Egypt", 0.0, 0.0)
    private var weatherResponsList = listOf(lastResponse)
    private var favLocationList = listOf(favLocation)

    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var fakeLocalDataSource: FakeLocalDataSource
    private lateinit var repository: Repository

    @Before
    fun createRepository() {
        fakeRemoteDataSource = FakeRemoteDataSource()
        fakeLocalDataSource =
            FakeLocalDataSource(
                weatherResponsList?.toMutableList(),
                favLocationList?.toMutableList()
            )
        repository = Repository(
            fakeRemoteDataSource,
            fakeLocalDataSource,
        )
    }

    @Test
    fun deleteLocation_returnNullOrEmptyList() = runBlockingTest() {
        // then call the method Delete the location
        repository.deleteLocation()
        // Get the least weather
        var lastWeather = repository.getLastWeather()
        // Collect the emitted value
        var result: WeatherResponse? = null
        lastWeather.collect { weatherResponse ->
            result = weatherResponse
        }
        // Assert that the result is  null
        assertNull(result)
    }

    @Test
    fun insertLeastWeatherResponse_takeWeatherResponse_returnIt() = runBlockingTest() {
        //given create a object
        val list: List<WeatherItem> = emptyList()
        val lastResponse = WeatherResponse(list, City("least Response"))

        // then call the method Delete the location
        repository.deleteLocation()
        repository.insertWeather(lastResponse)

        // Get the least weather
        var lastWeather = repository.getLastWeather()
        // Collect the emitted value
        var result: WeatherResponse? = null
        lastWeather.collect { weatherResponse ->
            result = weatherResponse
        }
        // Assert that the result is not null
        assertNotNull(result)
        // Assert specific properties of the result

        assertThat(result!!.city.name, `is`("least Response"))
        assertThat(result!!.list, `is`(emptyList()))
    }

    @Test
    fun getAllLocalLocation() = runBlockingTest() {
        // then call the method
        var flowOfLocations = repository.getAllFavLocation()

        // Collect the first emitted list of FavLocation
        var firstFavLocation: FavLocation? = null

        flowOfLocations.collect { listOfFavLocation ->
            if (listOfFavLocation.isNotEmpty()) {
                firstFavLocation = listOfFavLocation.first()
            }
        }

        // Assert that the firstFavLocation is not null
        assertNotNull(firstFavLocation)

        // Assert locationName is Egypt
        assertThat(firstFavLocation!!.locationName, `is`("Egypt"))
        assertThat(firstFavLocation!!.latitude, `is`(0.0))
        assertThat(firstFavLocation!!.longitude, `is`(0.0))

    }


    @Test
    fun `getLastWeather _ return flow of weather response have one last values has Alex as city name`() =
        runBlockingTest() {
            // then call the method and give default parameter
            var lastWeather = repository.getLastWeather()
            // Collect the emitted value
            var result: WeatherResponse? = null
            lastWeather.collect { weatherResponse ->
                result = weatherResponse
            }
            // Assert that the result is not null
            assertNotNull(result)
            // Assert specific properties of the result
            assertThat(result!!.city.name, `is`("Alex"))
            assertThat(result!!.list, `is`(emptyList()))

        }

    @Test
    fun `getWeather take latitude And Longitude And units and lang returnf WeatherResponse from Api`() =
        runBlockingTest() {
            // then call the method and give default parameter
            var resultResponse = repository.getWeather(0.0, 0.0, "", "")

            // Collect the emitted value
            var result: WeatherResponse? = null
            resultResponse.collect { weatherResponse ->
                result = weatherResponse
            }
            // Assert that the result is not null
            assertNotNull(result)
            // Assert specific properties of the result
            assertThat(result!!.city.name, `is`("Alex"))
            assertThat(result!!.list, `is`(emptyList()))

        }
}
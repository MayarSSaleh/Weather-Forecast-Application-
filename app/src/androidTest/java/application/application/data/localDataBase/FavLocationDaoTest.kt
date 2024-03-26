package application.application.data.localDataBase

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import application.application.MainCoroutineRule
import application.data.localDataBase.AppDataBase
import application.data.localDataBase.FavLocationsDao
import application.model.FavLocation
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest // unit test
class FavLocationDaoTest {
    lateinit var database: AppDataBase
    lateinit var dao: FavLocationsDao

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        //to avoid change the real data
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDataBase::class.java
        ).build()
        dao = database.getLocationDao()
    }

    @After
    fun finish() {
        database.close()
    }

    @Test
    fun getALL_ReturnNull() = runTest {
        // when
        val getResult = dao.getAll()
        val result = getResult.first()
        //assert
        assertNotNull(result)
        assertThat(result, `is`(emptyList<FavLocation>()))
    }

    @Test
    fun insertionFavLocation_FavLocation_getALLReturn() = runTest {
        // Given
        val favLocations = FavLocation("alex", 0.0, 0.0)
        dao.insert(favLocations)
        val getResult = dao.getAll()
        val result = getResult.first()
        assertThat(result.size, `is`(1))
        assertThat(result.get(0).locationName, `is`("alex"))
        assertThat(result.get(0).longitude, `is`(0.0))
        assertThat(result.get(0).latitude, `is`(0.0))
    }

    @Test
    fun getAllFavLocation_theFavouriteLocations_sizeOfListIsOne() = runBlockingTest {
        // Given
        val favLocation = FavLocation("alex", 0.0, 0.0)
        dao.insert(favLocation)
        // When
        val getResult = dao.getAll()
        var result: List<FavLocation>? = null
        getResult.collect { favLocationsList ->
            result = favLocationsList
            // Then
            assertThat(result?.size, `is`(1))
        }
    }

    @Test
    fun deleteFavLocation_FavLocation_getNull() = runTest {
        // Given
        val favLocations = FavLocation("alex", 0.0, 0.0)
        dao.insert(favLocations)
        dao.delete(favLocations)
        val getResult = dao.getAll()
        val result = getResult.first()
        assertNotNull(result)
        assertThat(result, `is`(emptyList<FavLocation>()))
    }
}
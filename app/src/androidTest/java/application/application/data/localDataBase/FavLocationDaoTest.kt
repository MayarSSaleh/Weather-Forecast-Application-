package application.application.data.localDataBase

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import application.application.MainCoroutineRule
import application.data.localDataBase.AppDataBase
import application.data.localDataBase.FavLocationsDao
import application.model.FavLocation
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
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

    @ExperimentalCoroutinesApi
    @get:Rule
    val rule = InstantTaskExecutorRule()

//    @ExperimentalCoroutinesApi
//    @get: Rule
//    var mainCoroutineRule = MainCoroutineRule()


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
    fun getALLReturnNull() {
        val dispatcher = TestCoroutineDispatcher()
        try {
            dispatcher.runBlockingTest {
                // Given
                val getResult = dao.getAll()
                var result: List<FavLocation>? = null

                // When
                val job = launch {
                    getResult.collect { favLocationsList ->
                        result = favLocationsList
                        Log.d("e", "$result")
                        assertThat(result?.size ?: 0, `is`(0))
                    }
                }
                // Ensure the coroutine completes before proceeding
                job.join()
                // Then
            }
        } catch (e: Exception) {
            println("Exception occurred: $e")
            Log.d("e", "$e")
        }
    }

//    fun getALLReturnNull() = runTest {
//        //give
//        val getResult = dao.getAll()
//        var result: List<FavLocation>? = null
//        // When
//        var job: Job = launch {
//            getResult.collect { favLocationsList ->
//                result = favLocationsList
//            }
//        }
//        // Then
//        job.join()
//        assertThat(result?.size ?: 0, `is`(0))
//    }

    @Test
    fun getAllFavLoction_theFavouriteLocations_sizeOfList() = runTest {
        //give
        val favLocations = FavLocation("alex", 0.0, 0.0)
        dao.insert(favLocations)
        //when
        val getResult = dao.getAll()
//        Flow<List<FavLocation>>
        var result: List<FavLocation>?
        //when
        getResult.collect { favLocationsList ->
            result = favLocationsList
            //assert
            assertThat(result?.size, `is`(1))
        }
    }

    @Test
    fun insertMethod_createObject_returnTheValesOfCreatedObject() =
        runBlockingTest {
            //given
            val favLocations = FavLocation("alex", 0.0, 0.0)
            dao.insert(favLocations)
            //when
            val getResult = dao.getAll()
//        Flow<List<FavLocation>>
            var result: List<FavLocation>?

            getResult.collect { favLocationsList ->
                result = favLocationsList
                // Assert that the result is not null
                assertNotNull(result)
                //then
                assertThat(result?.get(0)?.locationName, `is`("alex"))
            }
        }

    @Test
    fun deleteFavLoctionThenCreateObjectThenDeleteThenReturnNull() = runBlockingTest {
        val favLocations = FavLocation("alex", 0.0, 0.0)
        dao.insert(favLocations)
        //when
        dao.delete(favLocations)
        val getResult = dao.getAll()
//        Flow<List<FavLocation>>
        var result: List<FavLocation>?

        getResult.collect { favLocationsList ->
            result = favLocationsList
            assertThat(result?.size, `is`(0))
        }
    }

}

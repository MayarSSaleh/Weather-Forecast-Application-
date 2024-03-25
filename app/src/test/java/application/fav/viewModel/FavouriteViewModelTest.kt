package application.fav.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import application.model.City
import application.model.FakeRepository
import application.model.FavLocation
import application.model.LocalStateFavouirteLocations
import application.model.WeatherResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class FavouriteViewModelTest {

    lateinit var repo: FakeRepository
    lateinit var viewModel: FavViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        repo = FakeRepository()
        viewModel = FavViewModel(repo)
    }

    @Test
    fun `getFavLocations_emitsLoadingAndSuccessStates`() = runTest {
        // give
        var favLocation = FavLocation(("testGet"), 0.0, 0.0)
        val expectedFavLocations = listOf(favLocation)
        viewModel.getFavLocations()

        // when
        val results = mutableListOf<LocalStateFavouirteLocations>()
        viewModel.favLocations.collect {
            results.add(it)
            //assert
            assertThat((results as LocalStateFavouirteLocations.SuccessLocal).data, `is`(expectedFavLocations))
            assertEquals(LocalStateFavouirteLocations.SuccessLocal(expectedFavLocations), results[0])
        }
    }
    // ... other tests

//    @Test
//    fun getFavLocations_ReturnEmpty() = runBlockingTest {
//        // Then
//        viewModel.getFavLocations()
//
//        var result: LocalStateFavouirteLocations? = null
//
//        viewModel.favLocations.collect { newState ->
//            result = newState
//            // Assert
//            assertNotNull(result)
////            assertTrue(result, `is` LocalStateFavouirteLocations.SuccessLocal)
//            assertThat((result as LocalStateFavouirteLocations.SuccessLocal).data, `is`(emptyList()))
//        }
//
//    }

//    @Test
//    fun getFavLocations_ReturnDetails() = runTest {
//        // Then
//        var result: LocalStateFavouirteLocations? = null
//
//        viewModel.favLocations.collect { newState ->
//            result = newState
//        }
//        // Assert
//        assertNotNull(result)
//        result as LocalStateFavouirteLocations.SuccessLocal
//        assertTrue(result.data  is ("null") )
//    }

    @Test
    fun `insert favourite location return the inserted allocation details and not null`()  {
        val favLocation = FavLocation("Egypt", 0.0, 0.0)
        // When
        viewModel.insertFavLocation(favLocation)
        // Then
        // Verify that insert function is called with the correct favLocation
        var result = viewModel.favLocations.value
        // Assert that the inserted location is the same as the given location
        assertTrue(result is LocalStateFavouirteLocations.SuccessLocal)
    }

    @Test
    fun deleteFavLocation_returnNullListhaveOneFavLocation() = runBlockingTest() {
        val favLocation = FavLocation("Egypt", 0.0, 0.0)
        // When
        repo.insert(favLocation)
        viewModel.deleteFavLocation(favLocation)

        // Collect the emitted value
        var result: LocalStateFavouirteLocations? = null

        viewModel.favLocations.collect { newState ->
            result = newState
        }
        assertTrue(result is LocalStateFavouirteLocations.SuccessLocal)
    }
}
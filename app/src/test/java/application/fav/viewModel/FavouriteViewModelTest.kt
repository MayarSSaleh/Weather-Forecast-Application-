package application.fav.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import application.getOrAwaitValue
import application.model.FakeRepository
import application.model.FavLocation
import application.model.LocalStateFavouriteLocations
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavouriteViewModelTest {

    lateinit var repo: FakeRepository
    lateinit var viewModel: FavViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

//    @get:Rule
//    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        repo = FakeRepository()
        viewModel = FavViewModel(repo)
    }


    @Test
    fun `get favourite location return emptyList`() =
        runBlockingTest {
            viewModel.getFavLocations()
            launch {
                viewModel.favLocations.collect {
                    when (it) {
                        is LocalStateFavouriteLocations.SuccessLocal -> {
                            assertThat(it.data.size, `is`(0))
                            cancel()
                        }

                        else -> {}
                    }
                }
            }
        }


    @Test
    fun `insert favourite location return the inserted allocation details and not null`() =  runBlockingTest {
          // given
            val favLocation = FavLocation("Egypt", 0.0, 0.0)
            // When
            viewModel.insertFavLocation(favLocation)
            viewModel.getFavLocations()
            launch { // without it the test is not completed yet
                viewModel.favLocations.collect {
                    when (it) {
                        is LocalStateFavouriteLocations.SuccessLocal -> {
                            //assert
                            assertThat(it.data.get(0).locationName, `is`("Egypt"))
                            assertThat(it.data.get(0).longitude, `is`(0.0))
                            assertThat(it.data.get(0).latitude, `is`(0.0))
                            cancel() // without it Test finished with active jobs:
                        }
                        else -> {}
                    }
                }
            }
        }


    @Test
    fun `delete favourite location give object then delete it return list size zero`() = runBlockingTest {
            //given
            val favLocation = FavLocation("Egypt", 0.0, 0.0)
            // When
            viewModel.insertFavLocation(favLocation)
            viewModel.deleteFavLocation(favLocation)
            viewModel.getFavLocations()
            val job = launch {
                viewModel.favLocations.collect {
                    when (it) {
                        is LocalStateFavouriteLocations.SuccessLocal -> {
                            assertThat(it.data.size, `is`(0))
                        }
                        else -> {}
                    }
                }
            }
            job.cancel()
        }























    @Test
    fun getFavLocations_withTimeoutOrNull_() = runBlockingTest {
        val fav = FavLocation(("testGet"), 0.0, 0.0)
        repo.insert(fav)
        // when
        var result = viewModel.favLocations.getOrAwaitValue { }
        val succ = result as LocalStateFavouriteLocations.SuccessLocal
        assertThat(result, notNullValue())
        assertThat(succ.data, `is`(listOf(fav)))
    }

    @Test
    fun getFavLocations_emitsLoadingThenSuccessStates() = runTest {
        // give
        val fav = FavLocation(("testGet"), 0.0, 0.0)
        val expectedFavLocations = listOf(fav)
        repo.insert(fav)
        viewModel.getFavLocations()

        var results = viewModel.favLocations.take(2).toList()
        assert(results[1] is LocalStateFavouriteLocations.SuccessLocal)
        assertEquals(
            (results[1] as LocalStateFavouriteLocations.SuccessLocal).data,
            expectedFavLocations
        )
    }

    @Test
    fun getFavL() = runTest {
        // give
        val fav = FavLocation(("testGet"), 0.0, 0.0)
        val expectedFavLocations = listOf(fav)
        repo.insert(fav)
        viewModel.getFavLocations()
        // when
        var results = viewModel.favLocations.take(2).toList()
        assert(results[0] is LocalStateFavouriteLocations.LoadingLocal)
        assert(results[1] is LocalStateFavouriteLocations.SuccessLocal)
        assertEquals(
            (results[1] as LocalStateFavouriteLocations.SuccessLocal).data,
            expectedFavLocations
        )
    }

    @Test
    fun getFavLocations_emitsLoadingAndSuccessStates() = runTest {
        // give
        val favLocation = FavLocation(("testGet"), 0.0, 0.0)
        val expectedFavLocations = listOf(favLocation)
        viewModel.getFavLocations()
        // when
        val results = mutableListOf<LocalStateFavouriteLocations>()
        viewModel.favLocations.collectLatest {
            results.add(it)
        }
        assert(results[0] is LocalStateFavouriteLocations.LoadingLocal)
        assert(results[1] is LocalStateFavouriteLocations.SuccessLocal)
        assertEquals(
            (results[1] as LocalStateFavouriteLocations.SuccessLocal).data,
            expectedFavLocations
        )
    }

    @Test
    fun getFavLocations_ReturnNull() = runTest {
        // Then
        var result: LocalStateFavouriteLocations? = null

        viewModel.favLocations.collectLatest { newState ->
            result = newState
        }
        // Assert
        assertNotNull(result)
        result as LocalStateFavouriteLocations.SuccessLocal
//        assertThat(result.data, `is` (emptyList()))
    }

    @Test
    fun `insert favourite location_ the inserted allocation details and not null`() =
        runTest() {
            val favLocation = FavLocation("Egypt", 0.0, 0.0)
            // When
            viewModel.insertFavLocation(favLocation)
            // Then
            // Verify that insert function is called with the correct favLocation
            var result = viewModel.favLocations.value
            // Assert that the inserted location is the same as the given location
            assertTrue(result is LocalStateFavouriteLocations.SuccessLocal)
            result as LocalStateFavouriteLocations.SuccessLocal
            assertThat(result.data.get(0).locationName, `is`("Egypt"))

        }

}
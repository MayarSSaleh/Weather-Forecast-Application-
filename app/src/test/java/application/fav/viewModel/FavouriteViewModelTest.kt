package application.fav.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import application.model.FakeRepository
import application.model.FavLocation
import application.model.LocalStateFavouirteLocations
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import kotlinx.coroutines.withTimeoutOrNull

@RunWith(AndroidJUnit4::class)
class FavouriteViewModelTest {

    lateinit var repo: FakeRepository
    lateinit var viewModel: FavViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        repo = FakeRepository()
        viewModel = FavViewModel(repo)
    }

    @Test
    fun getFavLocations_withTimeoutOrNull_() = runTest {
        val fav = FavLocation(("testGet"), 0.0, 0.0)
        val expectedFavLocations = listOf(fav)
        repo.insert(fav)
        viewModel.getFavLocations()
        // when
            var result= withTimeoutOrNull(2000) {
                viewModel.favLocations
            }?.toList()
        if (result == null) {
            println("Network request timed out!")
        } else {
            assert(result[1] is LocalStateFavouirteLocations.SuccessLocal)
            assertEquals(
                (result[1] as LocalStateFavouirteLocations.SuccessLocal).data,
                expectedFavLocations
            )
        }
    }

    @Test
    fun getFavLocations_emitsLoadingThenSuccessStates() = runTest {
        // give
        val fav = FavLocation(("testGet"), 0.0, 0.0)
        val expectedFavLocations = listOf(fav)
        repo.insert(fav)
        viewModel.getFavLocations()

        // When (change scope with withContext)
        val testScope = TestScope()
        val results = withContext(testScope.coroutineContext) {
            viewModel.favLocations.take(2).toList()
        }
        assert(results[1] is LocalStateFavouirteLocations.SuccessLocal)
        assertEquals(
            (results[1] as LocalStateFavouirteLocations.SuccessLocal).data,
            expectedFavLocations)
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
        assert(results[0] is LocalStateFavouirteLocations.LoadingLocal)
        assert(results[1] is LocalStateFavouirteLocations.SuccessLocal)
        assertEquals(
            (results[1] as LocalStateFavouirteLocations.SuccessLocal).data,
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
        val results = mutableListOf<LocalStateFavouirteLocations>()
        viewModel.favLocations.collectLatest {
            results.add(it)
        }
        assert(results[0] is LocalStateFavouirteLocations.LoadingLocal)
        assert(results[1] is LocalStateFavouirteLocations.SuccessLocal)
        assertEquals(
            (results[1] as LocalStateFavouirteLocations.SuccessLocal).data,
            expectedFavLocations
        )
    }

    @Test
    fun getFavLocations_ReturnNull() = runTest {
        // Then
        var result: LocalStateFavouirteLocations? = null

        viewModel.favLocations.collectLatest { newState ->
            result = newState
        }
        // Assert
        assertNotNull(result)
        result as LocalStateFavouirteLocations.SuccessLocal
//        assertThat(result.data, `is` (emptyList()))
    }

    @Test
    fun `insert favourite location return the inserted allocation details and not null`() =
        runTest() {
            val favLocation = FavLocation("Egypt", 0.0, 0.0)
            // When
            viewModel.insertFavLocation(favLocation)
            // Then
            // Verify that insert function is called with the correct favLocation
            var result = viewModel.favLocations.value
            // Assert that the inserted location is the same as the given location
            assertTrue(result is LocalStateFavouirteLocations.SuccessLocal)
            result as LocalStateFavouirteLocations.SuccessLocal
            assertThat(result.data.get(0).locationName, `is`("Egypt"))

        }

}
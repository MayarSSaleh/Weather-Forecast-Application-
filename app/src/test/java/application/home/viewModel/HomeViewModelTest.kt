package application.home.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {
//    lateinit var repo: FakeRepository
    lateinit var viewModel: HomeViewModel

// to make all work in same thread as flow /live data observe in another thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
//        repo = FakeRepository()
//        viewModel = HomeViewModel(repo)
    }

    @Test
    fun addNewTask_newTaskEventIsNotNull() {
        // When: Call the method under testing (addNewTask)
//        viewModel.addNewTask()
//
//        // Then: assert that newTasks Event has Triggered
//        val result = viewModel.newTaskEvent.getOrAwaitValue()
//        MatcherAssert.assertThat(result, CoreMatchers.notNullValue())
    }


}
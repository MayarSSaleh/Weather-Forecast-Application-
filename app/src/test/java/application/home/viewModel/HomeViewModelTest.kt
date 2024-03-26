package application.home.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import application.model.FakeRepository
import application.sharedBetweenHomeScreenAndSettingScreen.WeatherShowViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {

    lateinit var repo: FakeRepository
    lateinit var viewModel: WeatherShowViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        repo = FakeRepository()
        viewModel = WeatherShowViewModel(repo)
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
//package application.ViewModelOfShowWeatherDerails
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import application.application.MainCoroutineRule
//import application.model.FakeRepository
//import application.showWeatherDetailsViewModel.WeatherShowViewModel
//import org.junit.Before
//import org.junit.Rule
//import org.junit.runner.RunWith
//
//
//@RunWith(AndroidJUnit4::class)
//class WeatherShowViewModelTest {
//
//    lateinit var repo: FakeRepository
//    lateinit var viewModel: WeatherShowViewModel
//
//    @get:Rule
//    val rule = InstantTaskExecutorRule()
//
//    @get:Rule
//    val mainCoroutineRule = MainCoroutineRule()
//
//    @Before
//    fun setUp() {
//        repo = FakeRepository()
//        viewModel = WeatherShowViewModel(repo)
//    }
//
//
//
//
//
//}
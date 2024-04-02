package application.alert.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import application.alerts.viewModel.AlertViewModel
import application.application.MainCoroutineRule
import application.model.Alert
import application.model.FakeRepository
import application.model.LocalStateAlerts
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AlertViewModelTest {

    lateinit var repo: FakeRepository
    lateinit var viewModel: AlertViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        repo = FakeRepository()
        viewModel = AlertViewModel(repo)
    }


    @Test
    fun `get alerts return emptyList`() = runTest {
        //when
        viewModel.getAlerts()
        launch {
            viewModel.alertsList.collectLatest {
                when (it) {
                    is LocalStateAlerts.SuccessLocalAlert -> {
                        // assert

                        MatcherAssert.assertThat(it.data.size, `is`(0))
                        cancel()
                    }

                    else -> {}
                }
            }
        }
    }

    @Test
    fun `insert alert return the inserted alert details and not null and the inserted object details`() =
        runTest {
            //give
            val alert = Alert("Alex", 0.0, 0.0, "", "", "Alarm")
            // When
            viewModel.insertALert(alert)
            viewModel.getAlerts()
            launch {
                viewModel.alertsList.collectLatest {
                    when (it) {
                        is LocalStateAlerts.SuccessLocalAlert -> {
                            // assert
                            assertThat(it.data.size, `is`(1))
                            assertThat(it.data.get(0).alertlocationName, `is`("Alex"))
                            assertThat(it.data.get(0).alertlatitude, `is`(0.0))
                            assertThat(it.data.get(0).alertlongitude, `is`(0.0))
                            assertThat(it.data.get(0).day, `is`(""))
                            assertThat(it.data.get(0).time, `is`(""))
                            assertThat(it.data.get(0).typeOfAlarm, `is`("Alarm"))
                            cancel()
                        }

                        else -> {}
                    }
                }
            }
        }


    @Test
    fun `delete the inserted alarm , first insert object then delete return list size is 0`() =
        runBlockingTest {
            //give
            val alert = Alert("Alex", 0.0, 0.0, "", "", "Alarm")
            // When
            viewModel.insertALert(alert)
            viewModel.deleteALLAlarms()
            viewModel.getAlerts()
            launch {
                viewModel.alertsList.collectLatest {
                    when (it) {
                        is LocalStateAlerts.SuccessLocalAlert -> {
                            // assert
                            assertThat(it.data.size, `is`(0))
                            cancel()
                        }

                        else -> {}
                    }
                }
            }
        }

    @Test
    fun `delete the inserted notification , first insert object then delete return list size is 0`() =
        runBlockingTest {
            //give
            val alert = Alert("Alex", 0.0, 0.0, "", "", "Notification")
            // When
            viewModel.insertALert(alert)
            viewModel.deleteALLNotification()
            viewModel.getAlerts()
            launch {
                viewModel.alertsList.collectLatest {
                    when (it) {
                        is LocalStateAlerts.SuccessLocalAlert -> {
                            // assert
                            assertThat(it.data.size, `is`(0))
                            cancel()
                        }

                        else -> {}
                    }
                }
            }
        }
}
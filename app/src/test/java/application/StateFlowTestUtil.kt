package application

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
suspend fun <T> Flow<T>.getOrAwaitValue(
    time: Long = 5,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)

    // Launch a coroutine to collect the flow
    val job = GlobalScope.launch {
        collect { value ->
            data = value
            latch.countDown()
            // Cancel the coroutine once the value is received
            cancel()
        }
    }

    try {
        afterObserve.invoke()

        // Don't wait indefinitely if the value is not emitted.
        if (!latch.await(time, timeUnit)) {
            job.cancel() // Cancel the job if timeout occurs
            throw TimeoutException("Flow value was never emitted.")
        }

    } finally {
        job.cancel() // Cancel the job in case of any failure
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

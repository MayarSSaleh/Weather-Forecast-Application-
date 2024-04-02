package application.application.data.localDataBase

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import application.data.localDataBase.AppDataBase
import application.data.localDataBase.FavLocationsDao
import application.model.FavLocation
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest // unit test
class FavLocationDaoTest {

    lateinit var database: AppDataBase
    lateinit var dao: FavLocationsDao


    @Before
    fun setUp() {
        //to avoid change the real data
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()
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
        assertThat(result, `is`(emptyList()))
    }

    @Test
    fun insertionFavLocation_FavLocation_getALLReturnTheAddedObject() = runTest {
        // Given
        val favLocations = FavLocation("alex", 0.0, 0.0)
        dao.insert(favLocations)
        //then
        val getResult = dao.getAll()
        val result = getResult.first()
        //assert
        assertThat(result.size, `is`(1))
        assertThat(result.get(0).locationName, `is`("alex"))
        assertThat(result.get(0).longitude, `is`(0.0))
        assertThat(result.get(0).latitude, `is`(0.0))
    }

    @Test
    fun deleteFavLocation_AddFavLocationThenDeleteIT_getNull() = runTest {
        // Given
        val favLocations = FavLocation("alex", 0.0, 0.0)
        dao.insert(favLocations)
        dao.delete(favLocations)
        //when
        val getResult = dao.getAll()
        val result = getResult.first()
        //assert
        assertNotNull(result)
        assertThat(result, `is`(emptyList()))
    }
}
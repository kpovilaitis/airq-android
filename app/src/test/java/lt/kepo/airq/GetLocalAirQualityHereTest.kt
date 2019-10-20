package lt.kepo.airq

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.rule.CoroutineTestRule
import lt.kepo.airq.ui.viewmodel.AirQualityViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class GetLocalAirQualityHereTest {
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule val rule = InstantTaskExecutorRule()
    @get:Rule val scopeRule = CoroutineTestRule()

    private lateinit var viewModel: AirQualityViewModel
    private lateinit var observer: Observer<AirQuality>

    @Before
    fun before() {
        Dispatchers.setMain(mainThreadSurrogate)

        val application : Application = mock()
        observer = mock()
        viewModel = AirQualityViewModel(AirQualityRepositoryMock(), application)

        viewModel.airQuality.observeForever(observer)
    }

    @Test
    fun getLocalAirQuality_ShouldReturnLocalAirQuality() = runBlockingTest {
        val expectedUser = AirQualityRepositoryMock.airQualityLocal

        scopeRule.launch(Dispatchers.Main) { // Will be launched in the mainThreadSurrogate dispatcher
//            viewModel.getLocalAirQualityHere()

            ArgumentCaptor.forClass(AirQuality::class.java).run {
                verify(observer, times(1)).onChanged(capture())
                assertEquals(expectedUser, value)
            }
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}
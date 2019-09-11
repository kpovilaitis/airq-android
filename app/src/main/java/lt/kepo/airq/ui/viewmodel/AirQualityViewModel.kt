package lt.kepo.airq.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import lt.kepo.airq.db.model.AirQuality
import lt.kepo.airq.repository.AirQualityRepository
import retrofit2.HttpException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

/**
 * The ViewModel used in [AirQualityFragment].
 */
class AirQualityViewModel(private val airQualityRepository: AirQualityRepository) : ViewModel(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    val airQuality = MutableLiveData<AirQuality>()
    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>(false)

    init {
        launch { airQuality.value = withContext(Dispatchers.IO) { airQualityRepository.getLocalHere() } }
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
        job.cancel()
    }

    fun getRemoteAirQualityHere() {
        launch {
            isLoading.value = true

            try {
                airQuality.value = withContext(Dispatchers.IO) { airQualityRepository.getRemoteHere() }

                isError.value = false

            } catch (e: HttpException) {
                println(e)

                isError.value = true
            } catch (e: UnknownHostException) {
                println(e)

                isError.value = true
            }

            isLoading.value = false
        }
    }
}

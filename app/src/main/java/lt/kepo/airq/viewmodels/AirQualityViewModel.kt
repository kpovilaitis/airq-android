package lt.kepo.airq.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.*
import lt.kepo.airq.data.models.AirQuality
import lt.kepo.airq.data.airquality.AirQualityRepository
import kotlin.coroutines.CoroutineContext

/**
 * The ViewModel used in [PlantDetailFragment].
 */
class AirQualityViewModel(airQualityRepository: AirQualityRepository) : ViewModel(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val stationId: Int = 0
//    val airQuality: LiveData<AirQuality> = airQualityRepository.getByStationId(stationId)
    val airQuality = MutableLiveData<AirQuality>()

    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }

    init {

        /* The getGardenPlantingForPlant method returns a LiveData from querying the database. The
         * method can return null in two cases: when the database query is running and if no records
         * are found. In these cases isPlanted is false. If a record is found then isPlanted is
         * true. */

    }

    fun addPlantToGarden() {
        viewModelScope.launch {
//            gardenPlantingRepository.createGardenPlanting(plantId)
        }
    }
}

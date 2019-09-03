package lt.kepo.airq.data.airquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import lt.kepo.airq.api.ApiClientService
import lt.kepo.airq.data.models.AirQuality

class AirQualityRepositoryImpl internal constructor(
    private val airQualityDao: AirQualityDao,
    private val airQualityService: ApiClientService
): AirQualityRepository {

//    override suspend fun getByStationId(stationId: Int) = airQualityDao.getByStationId(stationId)
    override suspend fun getByStationId(stationId: Int): LiveData<AirQuality> {
        return MutableLiveData<AirQuality>()

//        return try {
//            val result = airQualityService.getAirQualityHere().await()
//            UseCaseResult.Success(result)
//        } catch (ex: Exception) {
//            UseCaseResult.Error(ex)
//        }
    }

    override suspend fun insert(airQuality: AirQuality) = airQualityDao.insert(airQuality)
}
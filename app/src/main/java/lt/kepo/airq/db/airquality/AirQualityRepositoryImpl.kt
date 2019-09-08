package lt.kepo.airq.db.airquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import lt.kepo.airq.api.ApiClient
import lt.kepo.airq.api.ApiResponse
import lt.kepo.airq.data.models.AirQuality

class AirQualityRepositoryImpl internal constructor(
    private val airQualityDao: AirQualityDao,
    private val apiClient: ApiClient
): AirQualityRepository {

    //    override suspend fun getByStationIdFromApi(stationId: Int) = airQualityDao.getByStationIdFromApi(stationId)
    override suspend fun getByStationIdFromApi(stationId: Int): LiveData<AirQuality> {
        return MutableLiveData<AirQuality>()

//        return try {
//            val result = apiClient.getHere().await()
//            UseCaseResult.Success(result)
//        } catch (ex: Exception) {
//            UseCaseResult.Error(ex)
//        }
    }

    override suspend fun getHereFromApi(): ApiResponse<AirQuality> = apiClient.getHere()

    override suspend fun getHereFromDb(): AirQuality = airQualityDao.getHere()

    override suspend fun insert(airQuality: AirQuality) = airQualityDao.insert(airQuality)
}
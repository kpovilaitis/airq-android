package lt.kepo.airq.data.repository.airquality

import android.location.Location
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.api.HttpClient
import lt.kepo.airq.data.db.dao.AirQualityDao
import lt.kepo.airq.data.model.AirQuality
import java.lang.Exception
import java.util.*

class AirQualityRepositoryImpl internal constructor(
    private val airQualityDao: AirQualityDao,
    private val httpClient: HttpClient
) : AirQualityRepository {
    override suspend fun getRemoteAirQualityHere(location: Location?): ApiResponse<AirQuality> {
        return try {
            return if (location != null) {
                ApiResponse.parse(httpClient.getAirQualityHere("geo:${location.latitude};${location.longitude}"))
            } else {
                ApiResponse.parse(httpClient.getAirQualityHere())
            }
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }

    override suspend fun getRemoteAirQuality(stationId: Int): ApiResponse<AirQuality> {
        return try {
            ApiResponse.parse(httpClient.getStation("@${stationId}"))
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }

    override suspend fun insertLocalAirQuality(airQuality: AirQuality) = airQualityDao.insertWithTimeStamp(airQuality)

    override fun getLocalAirQualities() = airQualityDao.getAll()

    override fun getLocalAirQualityHere() = airQualityDao.getHere()

    override fun getLocalAirQuality(stationId: Int) = airQualityDao.get(stationId)

    override suspend fun getLocalAirQualityHereId() = withContext(Dispatchers.IO) { airQualityDao.getHereId() }

    override suspend fun deleteLocalAirQualityHere() = airQualityDao.deleteHere()

    override suspend fun deleteLocalAirQuality(stationId: Int) = airQualityDao.delete(stationId)
}

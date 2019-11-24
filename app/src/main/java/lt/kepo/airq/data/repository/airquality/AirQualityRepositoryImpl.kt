package lt.kepo.airq.data.repository.airquality

import android.location.Location
import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.api.HttpClient
import lt.kepo.airq.data.db.dao.AirQualityDao
import lt.kepo.airq.data.model.AirQuality
import java.lang.Exception

class AirQualityRepositoryImpl internal constructor(
    private val airQualityDao: AirQualityDao,
    private val httpClient: HttpClient
) : AirQualityRepository {
    override suspend fun deleteLocalAirQuality(stationId: Int) = airQualityDao.delete(stationId)

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

    override suspend fun getLocalAirQualities(): List<AirQuality> = airQualityDao.getAll()

    override suspend fun insertLocalAirQuality(airQuality: AirQuality) = airQualityDao.insert(airQuality)

    override suspend fun deleteLocalAirQualityHere() = airQualityDao.deleteHere()

    override suspend fun upsertLocalAirQuality(airQuality: AirQuality) = airQualityDao.upsert(airQuality)
}


package lt.kepo.airq.repository.airquality

import android.location.Location
import lt.kepo.airq.api.ApiResponse
import lt.kepo.airq.api.HttpClient
import lt.kepo.airq.api.dto.AirQualityDto
import lt.kepo.airq.db.dao.AirQualityDao
import lt.kepo.airq.db.model.AirQuality
import java.lang.Exception

class AirQualityRepositoryImpl internal constructor(
    private val airQualityDao: AirQualityDao,
    private val httpClient: HttpClient
) : AirQualityRepository {
    override suspend fun getRemoteAirQualityHere(): ApiResponse<AirQualityDto> {
        return try {
            ApiResponse.parse(httpClient.getAirQualityHere())
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }

    override suspend fun getRemoteAirQualityHere(location: Location): ApiResponse<AirQualityDto> {
        return try {
            ApiResponse.parse(httpClient.getAirQualityHere("geo:${location.latitude};${location.longitude}"))
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }

    override suspend fun getRemoteAirQuality(stationId: Int): AirQuality {
        TODO("not implemented")
    }

    override suspend fun getLocalAirQualityHere(): AirQuality = airQualityDao.getHere()

    override suspend fun getLocalAirQuality(stationId: Int): AirQuality = airQualityDao.getByStationId(stationId)

    override suspend fun upsertAirQualityHere(airQuality: AirQuality) = airQualityDao.upsertHere(airQuality)
}


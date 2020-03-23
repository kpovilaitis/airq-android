package lt.kepo.airquality.domain

import android.location.Location
import lt.kepo.airquality.domain.AirQualityRepository
import lt.kepo.core.network.ApiResponse
import lt.kepo.core.network.HttpClient
import lt.kepo.core.database.dao.AirQualityDao
import lt.kepo.core.model.AirQuality
import lt.kepo.core.constants.AIR_QUALITY_HERE_STATION_ID
import lt.kepo.core.network.mapOnSuccess
import java.lang.Exception
import java.util.*

class AirQualityRepositoryImpl (
    private val airQualityDao: AirQualityDao,
    private val httpClient: HttpClient
) : AirQualityRepository {
    override suspend fun updateAirQualityHere(location: Location?): ApiResponse<AirQuality> {
        return try {
            val response = if (location != null) {
                ApiResponse.parse(httpClient.getAirQualityHere("geo:${location.latitude};${location.longitude}"))
            } else {
                ApiResponse.parse(httpClient.getAirQualityHere())
            }

            response.mapOnSuccess {
                val quality = it.copy(
                    stationId = AIR_QUALITY_HERE_STATION_ID,
                    updatedAt = Date()
                )

                airQualityDao.insert(quality)

                quality
            }
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }

    override suspend fun updateAirQuality(stationId: Int): ApiResponse<AirQuality> {
        return try {
            ApiResponse.parse(httpClient.getAirQuality("@${stationId}")).mapOnSuccess {
                val quality = it.copy(updatedAt = Date())

                airQualityDao.insert(quality)
                quality
            }
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }

    override fun getCachedAirQualitiesLive() = airQualityDao.getAllLive()

    override suspend fun getCachedAirQualities() = airQualityDao.getAll()



    override fun getCachedAirQuality(stationId: Int) = airQualityDao.getLive(stationId)

    override suspend fun deleteCachedAirQuality(stationId: Int) = airQualityDao.delete(stationId)
}

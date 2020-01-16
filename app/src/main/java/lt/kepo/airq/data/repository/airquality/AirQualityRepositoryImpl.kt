package lt.kepo.airq.data.repository.airquality

import android.location.Location
import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.api.HttpClient
import lt.kepo.airq.data.api.mapOnSuccess
import lt.kepo.airq.data.db.dao.AirQualityDao
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.utility.AIR_QUALITY_HERE_STATION_ID
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



    override suspend fun addAirQuality(stationId: Int): ApiResponse<AirQuality> {
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
}

package lt.kepo.stations.repository

import lt.kepo.core.model.AirQuality
import lt.kepo.core.model.Station
import lt.kepo.core.network.ApiResponse
import lt.kepo.core.network.mapOnSuccess
import java.lang.Exception
import java.util.*

class StationsRepositoryImpl (
    private val airQualityDao: lt.kepo.core.database.dao.AirQualityDao,
    private val httpClient: lt.kepo.core.network.HttpClient
) : StationsRepository {
    override suspend fun getRemoteStations(query: String): ApiResponse<MutableList<Station>> {
        return try {
            ApiResponse.parse(httpClient.getStations(query))
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }

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
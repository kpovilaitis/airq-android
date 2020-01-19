package lt.kepo.airq.data.repository.stations

import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.api.HttpClient
import lt.kepo.airq.data.api.mapOnSuccess
import lt.kepo.airq.data.db.dao.AirQualityDao
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.model.Station
import java.lang.Exception
import java.util.*

class StationsRepositoryImpl (
    private val airQualityDao: AirQualityDao,
    private val httpClient: HttpClient
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
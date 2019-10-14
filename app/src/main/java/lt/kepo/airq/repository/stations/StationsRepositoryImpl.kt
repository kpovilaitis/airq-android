package lt.kepo.airq.repository.stations

import lt.kepo.airq.api.ApiResponse
import lt.kepo.airq.api.HttpClient
import lt.kepo.airq.api.dto.AirQualityDto
import lt.kepo.airq.api.dto.StationDto
import lt.kepo.airq.db.dao.StationDao
import lt.kepo.airq.db.model.Station
import java.lang.Exception

class StationsRepositoryImpl internal constructor(
    private val stationDao: StationDao,
    private val httpClient: HttpClient
) : StationsRepository {
    override suspend fun getRemoteStation(stationId: Int): ApiResponse<AirQualityDto> {
        return try {
            ApiResponse.parse(httpClient.getStation("@${stationId}"))
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }

    override suspend fun getRemoteStations(query: String): ApiResponse<List<StationDto>> {
        return try {
            ApiResponse.parse(httpClient.getStations(query))
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }

    override suspend fun getLocalAllStations(): List<Station> = stationDao.getAll()

    override suspend fun insertLocalStation(station: Station) = stationDao.upsert(station)

    override suspend fun deleteLocalStation(station: Station) = stationDao.delete(station)
}
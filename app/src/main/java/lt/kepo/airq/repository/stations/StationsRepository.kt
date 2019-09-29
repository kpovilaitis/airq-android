package lt.kepo.airq.repository.stations

import lt.kepo.airq.api.ApiResponse
import lt.kepo.airq.api.dto.AirQualityDto
import lt.kepo.airq.api.dto.StationDto
import lt.kepo.airq.db.model.Station

interface StationsRepository {
    suspend fun getRemoteStations(query: String): ApiResponse<List<StationDto>>

    suspend fun getLocalAllStations(): List<Station>

    suspend fun getStation(stationId: Int): ApiResponse<AirQualityDto>

    suspend fun insertStation(station: Station)

    suspend fun deleteStation(station: Station)
}
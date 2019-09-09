package lt.kepo.airq.repository

import lt.kepo.airq.db.model.AirQuality

interface AirQualityRepository {
    suspend fun getRemoteHere(): AirQuality

    suspend fun getRemoteByStationId(stationId: Int): AirQuality

    suspend fun getLocalHere(): AirQuality

    suspend fun getLocalByStationId(stationId: Int): AirQuality

    suspend fun insert(airQuality: AirQuality)
}
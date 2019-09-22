package lt.kepo.airq.repository.implementation

import android.location.Location
import lt.kepo.airq.api.ApiClient
import lt.kepo.airq.db.dao.AirQualityDao
import lt.kepo.airq.db.model.AirQuality
import lt.kepo.airq.repository.AirQualityRepository

class AirQualityRepositoryImpl internal constructor(
    private val airQualityDao: AirQualityDao,
    private val apiClient: ApiClient
) : AirQualityRepository {
    init { }

    override suspend fun getRemoteHere(): AirQuality {
        val response: AirQuality = AirQuality.build(apiClient.getAirQualityHere().body()!!.data)

        response.isCurrentLocationQuality = true

        airQualityDao.upsertHere(response)

        return response
    }

    override suspend fun getRemoteHereByLocation(location: Location): AirQuality {
        val response: AirQuality = AirQuality.build(
            apiClient.getAirQualityHereByLocation("geo:${location.latitude};${location.longitude}").body()!!.data
        )

        response.isCurrentLocationQuality = true

        airQualityDao.upsertHere(response)

        return response
    }

    override suspend fun getRemoteByStationId(stationId: Int): AirQuality {
        TODO("not implemented")
    }

    override suspend fun getLocalHere(): AirQuality = airQualityDao.getHere()

    override suspend fun getLocalByStationId(stationId: Int): AirQuality = airQualityDao.getByStationId(stationId)
}


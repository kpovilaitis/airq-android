package lt.kepo.airq.repository.implementation

import lt.kepo.airq.api.ApiClient
import lt.kepo.airq.db.dao.AirQualityDao
import lt.kepo.airq.db.model.AirQuality
import lt.kepo.airq.repository.AirQualityRepository

class AirQualityRepositoryImpl internal constructor(
    private val airQualityDao: AirQualityDao,
    private val apiClient: ApiClient
): AirQualityRepository {
    override suspend fun getRemoteHere(): AirQuality {
        val response = AirQuality.build(apiClient.getHere().body()!!.data)

        response.isCurrentLocationQuality = true

        airQualityDao.upsertHere(response)

        return response
    }

    override suspend fun getRemoteByStationId(stationId: Int): AirQuality {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLocalHere(): AirQuality = airQualityDao.getHere()

    override suspend fun getLocalByStationId(stationId: Int): AirQuality = airQualityDao.getByStationId(stationId)

    override suspend fun insert(airQuality: AirQuality): Long = airQualityDao.insert(airQuality)
}
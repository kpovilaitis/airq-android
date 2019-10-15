package lt.kepo.airq.data.repository.airquality

import android.location.Location
import androidx.lifecycle.LiveData
import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.api.HttpClient
import lt.kepo.airq.db.dao.AirQualityDao
import lt.kepo.airq.db.model.AirQuality
import java.lang.Exception

class AirQualityRepositoryImpl internal constructor(
    private val airQualityDao: AirQualityDao,
    private val httpClient: HttpClient
) : AirQualityRepository {
    override suspend fun getRemoteAirQualityHere(location: Location?): ApiResponse<AirQuality> {
        return try {
            return if (location != null) {
                ApiResponse.parse(httpClient.getAirQualityHere("geo:${location.latitude};${location.longitude}"))
            } else {
                ApiResponse.parse(httpClient.getAirQualityHere())
            }
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }

    override suspend fun getRemoteAirQuality(stationId: Int): AirQuality {
        TODO("not implemented")
    }

    override fun getLocalAirQualityHere(): LiveData<AirQuality> = airQualityDao.getHere()

    override suspend fun getLocalAirQuality(stationId: Int): LiveData<AirQuality> = airQualityDao.getByStationId(stationId)

    override suspend fun upsertLocalAirQualityHere(airQuality: AirQuality) = airQualityDao.upsertHere(airQuality)
}


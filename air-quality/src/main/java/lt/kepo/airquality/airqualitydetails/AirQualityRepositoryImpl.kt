package lt.kepo.airquality.airqualitydetails

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import lt.kepo.airquality.AirQuality
import lt.kepo.airquality.RefreshAirQualityHereUseCase
import lt.kepo.airquality.RefreshAirQualityUseCase
import lt.kepo.airquality.toDomainModel
import lt.kepo.airqualitydatabase.AirQualityDao
import javax.inject.Inject

class AirQualityRepositoryImpl @Inject constructor(
    private val airQualityDao: AirQualityDao,
    private val refreshAirQuality: RefreshAirQualityUseCase,
    private val refreshAirQualityHere: RefreshAirQualityHereUseCase
) : AirQualityRepository {

    override fun getAirQuality(
        stationId: Int
    ): Flow<AirQuality> =
        airQualityDao
            .get(stationId)
            .map { airQuality ->
                airQuality.toDomainModel()
            }

    override suspend fun remove(stationId: Int) {
        airQualityDao.delete(stationId)
    }

    override suspend fun refresh(
        stationId: Int
    ): AirQualityRepository.RefreshResult =
        airQualityDao
            .get(stationId)
            .first()
            .run {
                if (isCurrentLocationQuality) {
                    refreshAirQualityHere()
                } else {
                    refreshAirQuality(stationId)
                }
            }.let { refreshResult ->
                when (refreshResult) {
                    is RefreshAirQualityUseCase.Result.Success -> AirQualityRepository.RefreshResult.Success
                    is RefreshAirQualityHereUseCase.Result.Success -> AirQualityRepository.RefreshResult.Success
                    else -> AirQualityRepository.RefreshResult.Error
                }
            }


}

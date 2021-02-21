package lt.kepo.airquality

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import lt.kepo.airqualitydatabase.AirQualityDao
import javax.inject.Inject

class DatabaseAirQualitiesRepository @Inject constructor(
    private val airQualityDao: AirQualityDao,
    private val refreshAirQuality: RefreshAirQualityUseCase,
    private val refreshAirQualityHere: RefreshAirQualityHereUseCase
) : AirQualitiesRepository {

    override fun getAll(): Flow<List<AirQuality>> =
        airQualityDao
            .getAll()
            .map { airQualityEntities ->
                airQualityEntities.map { airQualityEntity ->
                    airQualityEntity.toDomainModel()
                }
            }

    override fun getAirQuality(stationId: Int): Flow<AirQuality> =
        airQualityDao
            .get(stationId)
            .map { it.toDomainModel() }

    override suspend fun remove(stationId: Int) {
        airQualityDao.delete(stationId)
    }

    override suspend fun refresh(): AirQualitiesRepository.RefreshResult = coroutineScope {
        airQualityDao
            .getAll()
            .first()
            .map { entity ->
                async {
                    if (entity.isCurrentLocationQuality) {
                        refreshAirQualityHere()
                    } else {
                        refreshAirQuality(entity.stationId)
                    }
                }
            }.awaitAll()
            .let { refreshResults ->
                when {
                    refreshResults.any { it is RefreshAirQualityUseCase.Result.Error } ->
                        AirQualitiesRepository.RefreshResult.Error
                    refreshResults.any { it is RefreshAirQualityHereUseCase.Result.Error } ->
                        AirQualitiesRepository.RefreshResult.Error
                    else ->
                        AirQualitiesRepository.RefreshResult.Success
                }
            }
    }
}

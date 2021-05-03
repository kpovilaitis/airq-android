package lt.kepo.airquality

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
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

    override fun getAirQuality(stationId: Int): Flow<AirQuality?> =
        airQualityDao
            .get(stationId)
            .map { it?.toDomainModel() }

    override suspend fun remove(stationId: Int) {
        airQualityDao.delete(stationId)
    }

    override suspend fun refresh(): AirQualitiesRepository.RefreshResult = coroutineScope {
        airQualityDao
            .getAll()
            .first()
            .filterNot { entity -> entity.isCurrentLocationQuality }
            .map { entity ->
                async {
                    refreshAirQuality(entity.stationId)
                }
            }.plus(
                async {
                    refreshAirQualityHere()
                }
            ).awaitAll()
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

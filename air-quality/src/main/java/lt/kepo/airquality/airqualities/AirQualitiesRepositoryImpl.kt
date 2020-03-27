package lt.kepo.airquality.airqualities

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import lt.kepo.airquality.AirQuality
import lt.kepo.airquality.RefreshAirQualityHereUseCase
import lt.kepo.airquality.RefreshAirQualityUseCase
import lt.kepo.airquality.airqualitydetails.AirQualityRepository
import lt.kepo.airquality.toDomainModel
import lt.kepo.airqualitydatabase.AirQualityDao
import javax.inject.Inject

class AirQualitiesRepositoryImpl @Inject constructor(
    private val airQualityDao: AirQualityDao,
    private val refreshAirQuality: RefreshAirQualityUseCase,
    private val refreshAirQualityHere: RefreshAirQualityHereUseCase
) : AirQualitiesRepository {

    override fun getAirQualities(): Flow<List<AirQuality>> =
        airQualityDao
            .getAll()
            .map { airQualityEntities ->
                airQualityEntities.map { airQualityEntity ->
                    airQualityEntity.toDomainModel()
                }
            }

    override suspend fun refresh(): AirQualitiesRepository.RefreshResult = withContext(IO) {
        airQualityDao
            .getAll()
            .first()
            .filterNot { it.isCurrentLocationQuality }
            .map { entity ->
                async {
                    refreshAirQuality(entity.stationId)
                }
            }
            .plus(
                async {
                    refreshAirQualityHere()
                }
            )
            .awaitAll()
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

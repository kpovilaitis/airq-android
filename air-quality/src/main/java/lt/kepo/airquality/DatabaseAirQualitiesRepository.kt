package lt.kepo.airquality

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import lt.kepo.airqualitydatabase.AirQualityDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class DatabaseAirQualitiesRepository @Inject constructor(
    private val airQualityDao: AirQualityDao,
    private val refreshAirQuality: RefreshAirQualityUseCase,
    private val refreshAirQualityHere: RefreshAirQualityHereUseCase,
    private val expiresAfterMillis: Long,
    private val getCurrentTimeMillis: () -> Long,
    private val externalCoroutineContext: CoroutineContext,
) : AirQualitiesRepository, CoroutineScope {

    private var refreshedAtMillis: Long = 0

    private val _isRefreshing = MutableStateFlow(false)
    override val isRefreshing: Flow<Boolean> = _isRefreshing

    override val airQualities: Flow<List<AirQuality>> =
        airQualityDao
            .getAll()
            .onStart {

            }.map { airQualityEntities ->
                airQualityEntities.map { airQualityEntity ->
                    airQualityEntity.toDomainModel()
                }
            }

    override fun getAirQuality(stationId: Int): Flow<AirQuality?> =
        airQualityDao
            .get(stationId)
            .onStart {

            }.map { airQualityEntity ->
                airQualityEntity?.toDomainModel()
            }

    override suspend fun remove(stationId: Int) {
        airQualityDao.delete(stationId)
    }

    override suspend fun refresh() = coroutineScope {
        _isRefreshing.value = true
        airQualityDao
            .getAll()
            .first()
            .filterNot { entity ->
                entity.isCurrentLocationQuality
            }.map { entity ->
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
                    refreshResults.any { it is RefreshAirQualityUseCase.Result.Error } -> {
//                            AirQualitiesRepository.RefreshResult.Error
                    }
                    refreshResults.any { it is RefreshAirQualityHereUseCase.Result.Error } -> {
//                            AirQualitiesRepository.RefreshResult.Error
                    }
                    else -> {
//                            AirQualitiesRepository.RefreshResult.Success
                    }
                }
            }

        refreshedAtMillis = getCurrentTimeMillis()
        _isRefreshing.value = false
    }

    override val coroutineContext: CoroutineContext
        get() = TODO("Not yet implemented")
}

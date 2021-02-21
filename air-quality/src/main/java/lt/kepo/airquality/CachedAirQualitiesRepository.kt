package lt.kepo.airquality

import kotlinx.coroutines.flow.Flow
import lt.kepo.airquality.AirQualitiesRepository.RefreshResult
import javax.inject.Inject

internal class CachedAirQualitiesRepository @Inject constructor(
    private val originalRepository: AirQualitiesRepository,
    private val cacheExpiresAfterMillis: Long,
    private val getCurrentTimeMillis: () -> Long
) : AirQualitiesRepository, RefreshAirQualitiesCacheUseCase {

    private var cachedAtMillis: Long = 0

    override fun getAll(): Flow<List<AirQuality>> =
        originalRepository.getAll()

    override fun getAirQuality(stationId: Int): Flow<AirQuality> =
        originalRepository.getAirQuality(stationId)

    override suspend fun remove(stationId: Int) {
        originalRepository.remove(stationId)
    }

    override suspend fun refresh(): RefreshResult {
        val nowMillis = getCurrentTimeMillis()

        return if (cachedAtMillis + cacheExpiresAfterMillis < nowMillis) {
            cachedAtMillis = nowMillis
            originalRepository.refresh()
        } else {
            RefreshResult.Success
        }
    }

    override suspend fun invoke(): RefreshAirQualitiesCacheUseCase.Result =
        originalRepository
            .refresh()
            .toRefreshCacheResult()

    private fun RefreshResult.toRefreshCacheResult(): RefreshAirQualitiesCacheUseCase.Result =
        when (this) {
            is RefreshResult.Success -> RefreshAirQualitiesCacheUseCase.Result.Success
            is RefreshResult.Error -> RefreshAirQualitiesCacheUseCase.Result.Error
        }
}

internal fun AirQualitiesRepository.withCache(
    cacheExpiresAfterMillis: Long,
    getCurrentTimeMillis: () -> Long
): CachedAirQualitiesRepository = CachedAirQualitiesRepository(
    originalRepository = this,
    cacheExpiresAfterMillis = cacheExpiresAfterMillis,
    getCurrentTimeMillis = getCurrentTimeMillis
)

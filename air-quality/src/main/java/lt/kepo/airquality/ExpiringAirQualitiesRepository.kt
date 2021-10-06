package lt.kepo.airquality

import kotlinx.coroutines.flow.Flow
import lt.kepo.airquality.AirQualitiesRepository.RefreshResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ExpiringAirQualitiesRepository @Inject constructor(
    private val originalRepository: AirQualitiesRepository,
    private val expiresAfterMillis: Long,
    private val getCurrentTimeMillis: () -> Long
) : AirQualitiesRepository, IsAirQualitiesExpired {

    private var refreshedAtMillis: Long = 0

    override fun getAll(): Flow<List<AirQuality>> =
        originalRepository.getAll()

    override fun getAirQuality(stationId: Int): Flow<AirQuality?> =
        originalRepository.getAirQuality(stationId)

    override suspend fun remove(stationId: Int) {
        originalRepository.remove(stationId)
    }

    override suspend fun refresh(): RefreshResult {
        refreshedAtMillis = getCurrentTimeMillis()
        return originalRepository.refresh()
    }

    override fun invoke(): Boolean =
        getCurrentTimeMillis() > refreshedAtMillis + expiresAfterMillis
}

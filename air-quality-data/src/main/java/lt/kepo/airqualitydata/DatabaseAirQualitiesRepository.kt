package lt.kepo.airqualitydata

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import lt.kepo.airqualitydatabase.AirQualityDao
import lt.kepo.airqualitydata.refresh.RefreshAirQualityHereUseCase
import lt.kepo.airqualitydata.refresh.RefreshAirQualityUseCase
import lt.kepo.airqualitynetwork.AirQualityApi
import lt.kepo.airqualitynetwork.ApiResult
import lt.kepo.core.ApplicationScope
import lt.kepo.core.CurrentTime
import javax.inject.Inject

class DatabaseAirQualitiesRepository @Inject constructor(
    private val airQualityDao: AirQualityDao,
    private val refreshAirQuality: RefreshAirQualityUseCase,
    private val refreshAirQualityHere: RefreshAirQualityHereUseCase,
    private val airQualityApi: AirQualityApi,
    @CurrentTime private val getCurrentTimeMillis: () -> Long,
    @ApplicationScope private val applicationScope: CoroutineScope,
) : AirQualitiesRepository {

    private var refreshedAtMillis: Long = 0

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableSharedFlow<AirQualitiesRepository.Error>()
    override val error: Flow<AirQualitiesRepository.Error> = _error

    override val airQualities: Flow<List<AirQualityListItem>> = airQualityDao.getAll()
        .onStart {
            if (refreshedAtMillis < getCurrentTimeMillis() - EXPIRATION_DURATION) {
                applicationScope.launch {
                    refresh()
                }
            }
        }.map { airQualityEntities ->
            airQualityEntities.map { airQualityEntity ->
                airQualityEntity.toListItemModel()
            }
        }

    override fun getAirQuality(stationId: Int): Flow<AirQualityDetails?> = airQualityDao.get(stationId)
        .onStart {
            if (refreshedAtMillis < getCurrentTimeMillis() - EXPIRATION_DURATION) {
                applicationScope.launch {
                    refresh()
                }
            }
        }.map { airQualityEntity ->
            airQualityEntity?.toDetailsModel()
        }

    override suspend fun add(stationId: Int) {
        _isLoading.value = true
        airQualityApi.getAirQuality(stationId).let { apiResult ->
            _isLoading.value = false
            when (apiResult) {
                is ApiResult.Success -> {
                    airQualityDao.insert(
                        airQuality = apiResult.data.toEntityModel(
                            isCurrentLocationQuality = false,
                        ),
                    )
                }
                is ApiResult.Error -> {
                    _error.emit(AirQualitiesRepository.Error.Add)
                }
            }
        }
    }

    override suspend fun remove(stationId: Int) {
        airQualityDao.delete(stationId)
    }

    override suspend fun refresh() = coroutineScope {
        _isLoading.value = true
        val entities = airQualityDao
            .getAll()
            .first()
            .filterNot { entity ->
                entity.isCurrentLocationQuality
            }

        val refreshResults = listOf(
            async {
                refreshAirQualityHere()
            }
        ).plus(
            entities.map { entity ->
                async {
                    refreshAirQuality(entity.stationId)
                }
            }
        ).awaitAll()
        refreshedAtMillis = getCurrentTimeMillis()
        val isRefreshError = refreshResults.any { result ->
            result is RefreshAirQualityUseCase.Result.Error ||
                    result is RefreshAirQualityHereUseCase.Result.Error
        }

        _isLoading.value = false
        if (isRefreshError) {
            _error.emit(AirQualitiesRepository.Error.Refresh)
        }
    }

    companion object {
        private const val EXPIRATION_DURATION = 30 * 60 * 1000
    }
}

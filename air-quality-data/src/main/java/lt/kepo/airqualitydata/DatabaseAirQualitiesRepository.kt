package lt.kepo.airqualitydata

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import lt.kepo.airqualitydatabase.AirQualityDao
import lt.kepo.airqualitydata.AirQualitiesRepository.LoadState
import lt.kepo.airqualitydata.refresh.RefreshAirQualityHereUseCase
import lt.kepo.airqualitydata.refresh.RefreshAirQualityUseCase
import lt.kepo.airqualitynetwork.AirQualityApi
import lt.kepo.airqualitynetwork.ApiResult
import lt.kepo.airqualitynetwork.call
import javax.inject.Inject

class DatabaseAirQualitiesRepository @Inject constructor(
    private val airQualityDao: AirQualityDao,
    private val refreshAirQuality: RefreshAirQualityUseCase,
    private val refreshAirQualityHere: RefreshAirQualityHereUseCase,
    private val airQualityApi: AirQualityApi,
    private val getCurrentTimeMillis: () -> Long,
) : AirQualitiesRepository {

    private var refreshedAtMillis: Long = 0
    private val coroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default
    )

    private val _loadState = MutableStateFlow<LoadState>(LoadState.NotLoading)
    override val loadState: Flow<LoadState> = _loadState

    override val airQualities: Flow<List<AirQualityListItem>> =
        airQualityDao
            .getAll()
            .onStart {
                if (refreshedAtMillis < getCurrentTimeMillis() - EXPIRATION_DURATION) {
                    coroutineScope.launch {
                        refresh()
                    }
                }
            }.map { airQualityEntities ->
                airQualityEntities.map { airQualityEntity ->
                    airQualityEntity.toListItemModel()
                }
            }

    override fun getAirQuality(stationId: Int): Flow<AirQualityDetails?> =
        airQualityDao
            .get(stationId)
            .onStart {

            }.map { airQualityEntity ->
                airQualityEntity?.toDetailsModel()
            }

    override suspend fun add(stationId: Int) {
        _loadState.value = LoadState.Loading
        airQualityApi.call {
            getAirQuality(stationId)
        }.let { apiResult ->
            when (apiResult) {
                is ApiResult.Success -> {
                    airQualityDao.insert(
                        airQuality = apiResult.data.toEntityModel(
                            isCurrentLocationQuality = false,
                        ),
                    )
                    _loadState.value = LoadState.NotLoading
                }
                is ApiResult.Error -> {
                    _loadState.value = LoadState.Error.Add
                }
            }
        }
    }

    override suspend fun remove(stationId: Int) {
        airQualityDao.delete(stationId)
    }

    override suspend fun refresh() = coroutineScope {
        _loadState.value = LoadState.Loading
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
                refreshedAtMillis = getCurrentTimeMillis()
                val isRefreshError = refreshResults.any { result ->
                    result is RefreshAirQualityUseCase.Result.Error ||
                        result is RefreshAirQualityHereUseCase.Result.Error
                }

                _loadState.value = if (isRefreshError) {
                    LoadState.Error.Refresh
                } else {
                    LoadState.NotLoading
                }
            }
    }

    companion object {
        private const val EXPIRATION_DURATION = 30 * 60 * 1000
    }
}

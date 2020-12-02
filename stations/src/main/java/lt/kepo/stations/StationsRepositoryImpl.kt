package lt.kepo.stations

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class StationsRepositoryImpl @Inject constructor(
    private val getStationsUseCase: GetStationsUseCase,
    private val addStationUseCase: AddStationUseCase
) : StationsRepository {

    private val _stations = MutableStateFlow<List<Station>>(emptyList())
    override val stations: Flow<List<Station>> = _stations

    override suspend fun save(stationId: Int): StationsRepository.SaveResult =
        when (addStationUseCase(stationId)) {
            is AddStationUseCase.Result.Success -> {
                _stations.value = _stations.value.filterNot { it.id == stationId }
                StationsRepository.SaveResult.Success
            }
            is AddStationUseCase.Result.Error -> {
                StationsRepository.SaveResult.Error
            }
        }

    override suspend fun search(query: String): StationsRepository.SearchResult =
        when (val result = getStationsUseCase(query)) {
            is GetStationsUseCase.Result.Success -> {
                _stations.emit(result.stations)
                StationsRepository.SearchResult.Success
            }
            is GetStationsUseCase.Result.Error -> {
                StationsRepository.SearchResult.Error
            }
        }

    override suspend fun clear() {
        _stations.emit(emptyList())
    }
}
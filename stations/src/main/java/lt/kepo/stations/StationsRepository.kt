package lt.kepo.stations

import kotlinx.coroutines.flow.Flow

interface StationsRepository {

    val stations: Flow<List<Station>>

    suspend fun save(
        stationId: Int
    ): SaveResult

    suspend fun search(
        query: String
    ): SearchResult

    suspend fun clear()

    sealed class SearchResult {

        object Success : SearchResult()

        object Error : SearchResult()
    }

    sealed class SaveResult {

        object Success : SaveResult()

        object Error : SaveResult()
    }
}
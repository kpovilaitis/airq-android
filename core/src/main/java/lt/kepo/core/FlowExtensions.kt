package lt.kepo.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

fun <Y, T> MutableStateFlow<T>.addSource(
    scope: CoroutineScope,
    source: Flow<Y>,
    block: MutableStateFlow<T>.(source: Y) -> Unit,
): MutableStateFlow<T> = also {
    subscriptionCount
        .map { count -> count > 0 }
        .distinctUntilChanged()
        .flatMapLatest { isObserved ->
            if (isObserved) {
                source
            } else {
                emptyFlow()
            }
        }.onEach { sourceValue ->
            block(sourceValue)
        }.launchIn(scope)
}

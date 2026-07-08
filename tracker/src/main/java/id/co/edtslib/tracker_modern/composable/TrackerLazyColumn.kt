package id.co.edtslib.tracker_modern.composable

import android.util.Log
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.co.edtslib.tracker_modern.Tracker
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * A wrapper around [LazyColumn] that provides optional automatic impression tracking.
 * When an item in the list becomes visible on the screen, it can automatically send a
 * tracking event when scroll is idle
 * */
@Composable
fun <T, Mapped> TrackerLazyColumn(
    modifier: Modifier = Modifier,
    listData: ImmutableList<T>,
    enableImpressionTracking: Boolean = false,
    trackerCategory: String? = null,
    trackerMapper: ((T) -> Mapped)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    getListState: ((LazyListState)-> Unit)? = null,
    content: LazyListScope.() -> Unit,
) {
    val listState = if (enableImpressionTracking){
        rememberImpressionTracker(
            category = trackerCategory.orEmpty(),
            items = listData,
            mapper = trackerMapper
        ) { category, time, data, mapper ->
            Tracker.trackImpression(category, time, data, mapper)
            Log.i("TrackerLazyColumn", "Tracked: $category - $data at $time")
        }
    } else {
        rememberLazyListState()
    }
    getListState?.invoke(listState)

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        overscrollEffect = overscrollEffect,
    ) {
        content()
    }
}

@Stable
data class ImpressionData<T>(
    val data: ImmutableList<T>,
    val time: Long
)

/**
 * Remembers impression tracking state for a LazyColumn,
 * this function is equivalent with [Tracker.Companion.setImpressionRecyclerView]
 */
@Composable
private fun <S, T> rememberImpressionTracker(
    category: String,
    items: ImmutableList<S>,
    mapper: ((S) -> T)? = null,
    onTrackImpression: (category: String, time: Long, data: ImmutableList<*>, mapper: ((S) -> T)?) -> Unit
): LazyListState {
    val listState = rememberLazyListState()

    var firstImpression by remember { mutableIntStateOf(-1) }
    var lastImpression by remember { mutableIntStateOf(-1) }
    val pendingImpressions = remember { mutableListOf<ImpressionData<S>>() }

    // Track visible items when scrolling stops
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { scrolling ->
                if (!scrolling && pendingImpressions.isNotEmpty()){
                    pendingImpressions.forEach { impressionData ->
                        onTrackImpression(
                            category,
                            impressionData.time,
                            impressionData.data,
                            mapper
                        )
                    }
                    pendingImpressions.clear()
                }
            }
    }

    // Track visible range changes
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.let { visibleItems ->
                if (visibleItems.isEmpty()) null
                else visibleItems.first().index to visibleItems.last().index
            }
        }
            .distinctUntilChanged()
            .collect { range ->
                if (range != null) {
                    val (first, last) = range

                    if (firstImpression != first || lastImpression != last) {
                        firstImpression = first
                        lastImpression = last

                        // Collect visible items
                        val visibleItems = (first..last)
                            .mapNotNull { index -> items.getOrNull(index) }

                        if (visibleItems.isNotEmpty()) {
                            pendingImpressions.add(
                                ImpressionData(
                                    data = visibleItems.toImmutableList(),
                                    time = System.currentTimeMillis()
                                )
                            )
                        }
                    }
                }
            }
    }

    return listState
}
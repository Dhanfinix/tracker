package id.co.edtslib.tracker.composable

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.co.edtslib.tracker.Tracker
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.collections.plus

/**
 * A wrapper around [LazyColumn] that provides optional automatic impression tracking.
 * When an item in the list becomes visible on the screen, it can automatically send a
 * tracking event when scroll is idle
 * */
@Composable
fun <T> TrackerLazyColumn(
    modifier: Modifier = Modifier,
    listData: List<T>,
    enableImpressionTracking: Boolean = false,
    trackerCategory: String? = null,
    trackerMapper: ((T) -> String)? = null,
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
            Log.i("EdtsLazyColumn", "Tracked: $category - $data at $time")
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


data class ImpressionData<T>(
    val data: List<T>,
    val time: Long
)

/**
 * Remembers impression tracking state for a LazyColumn,
 * this function is equivalent with [id.co.edtslib.tracker.Tracker.setImpressionRecyclerView]
 */
@Composable
private fun <S, T> rememberImpressionTracker(
    category: String,
    items: List<S>,
    mapper: ((S) -> T)? = null,
    onTrackImpression: (category: String, time: Long, data: List<*>, mapper: ((S) -> T)?) -> Unit
): LazyListState {
    val listState = rememberLazyListState()

    var firstImpression by remember { mutableStateOf(-1) }
    var lastImpression by remember { mutableStateOf(-1) }
    var pendingImpressions by remember { mutableStateOf<List<ImpressionData<S>>>(emptyList()) }

    // Track visible items when scrolling stops
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && pendingImpressions.isNotEmpty()) {
            // Process all pending impressions as a batch
            pendingImpressions.forEach { impressionData ->
                onTrackImpression(
                    category,
                    impressionData.time,
                    impressionData.data,
                    mapper
                )
            }
            pendingImpressions = emptyList()
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

                    if (firstImpression != first && lastImpression != last) {
                        firstImpression = first
                        lastImpression = last

                        // Collect visible items
                        val visibleItems = mutableListOf<S>()
                        for (i in first..last) {
                            if (i in items.indices) {
                                visibleItems.add(items[i])
                            }
                        }

                        if (visibleItems.isNotEmpty()) {
                            val newImpression = ImpressionData(
                                data = visibleItems,
                                time = System.currentTimeMillis()
                            )
                            pendingImpressions = pendingImpressions + newImpression
                        }
                    }
                }
            }
    }

    return listState
}
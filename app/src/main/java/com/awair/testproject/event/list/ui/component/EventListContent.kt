package com.awair.testproject.event.list.ui.component

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.awair.testproject.R
import com.awair.testproject.event.list.data.entity.Event
import com.awair.testproject.event.list.ui.EventListFragment
import com.awair.testproject.event.list.ui.EventListViewModel.Companion.DATE_FORMAT_STRING_SECTION
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalMaterialApi
@Composable
fun EventListContent(
    context: Context, eventStateListMap: Map<Long, List<Pair<Event, Boolean>>>,
    isLoading: MutableState<Boolean>,
    bottomReachedCallback: () -> Unit,
    cardItemClick: (Event) -> Unit
) {
    val eventList = remember { eventStateListMap }
    val loading = isLoading.value
    val listState = rememberLazyListState()
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            for(key in eventList.keys.sorted()) {
                itemsIndexed(
                    items = eventList[key]!!,
                    itemContent = { index, item ->
                        if(index == 0) {
                            Text(
                                text = getSectionTitle(Date(key)),
                                style = MaterialTheme.typography.h4
                            )
                        }
                        EventListItem(
                            context = context,
                            eventConflictFlagPair = item,
                            cardItemClick = cardItemClick
                        )
                    })
            }
        }
        LoadingCircleView(isDisplayed = loading)
    }
    listState.OnBottomReached(
        buffer = EventListFragment.BOTTOM_ITEM_VISIBILITY_BUFFER_COUNT,
        loadMore = bottomReachedCallback
    )
}

@Composable
fun LazyListState.OnBottomReached(
    buffer: Int = 0,
    loadMore: () -> Unit
) {
    require(buffer >= 0) { "buffer cannot be negative, but was $buffer" }
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem =
                layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - buffer
        }
    }
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                if (it) loadMore()
            }
    }
}

private fun getSectionTitle(date: Date?) : String {
    val format = SimpleDateFormat(DATE_FORMAT_STRING_SECTION, Locale.US)
    return format.format(date?: Date())
}
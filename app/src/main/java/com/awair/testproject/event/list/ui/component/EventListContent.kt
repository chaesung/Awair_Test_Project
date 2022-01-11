package com.awair.testproject.event.list.ui.component

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.awair.testproject.R
import com.awair.testproject.event.list.data.entity.Event
import com.awair.testproject.event.list.ui.EventListFragment
import kotlinx.coroutines.flow.collect

@ExperimentalMaterialApi
@Composable
fun EventListContent(
    context: Context, eventStateList: SnapshotStateList<Pair<Event, Boolean>>,
    isLoading: MutableState<Boolean>,
    bottomReachedCallback: () -> Unit,
    cardItemClick: (Event) -> Unit
) {
    val eventList = remember { eventStateList }
    val loading = isLoading.value
    val listState = rememberLazyListState()
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                items = eventList,
                itemContent = {
                    EventListItem(context = context, eventConflictFlagPair = it, cardItemClick = cardItemClick)
                })
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
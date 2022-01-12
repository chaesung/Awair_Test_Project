package com.awair.testproject.event.list.ui.component

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.awair.testproject.R
import com.awair.testproject.event.list.data.entity.Event

/**
 * Event list item compose view
 * Displays title, start time, end time
 * If title is empty it will display as No Title
 */
@ExperimentalMaterialApi
@Composable
fun EventListItem(
    context: Context, eventConflictFlagPair: Pair<Event, Boolean>,
    cardItemClick: (Event) -> Unit
) {
    val event = eventConflictFlagPair.first
    val isConflict = eventConflictFlagPair.second
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.White.takeUnless { isConflict }?: Color.Magenta,
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        onClick = {
            cardItemClick(event)
        }
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = event.title.takeUnless { it.isEmpty() }
                        ?: context.getString(R.string.no_title),
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = "${context.getString(R.string.start_time)}: ${event.start}",
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "${context.getString(R.string.end_time)}: ${event.end}",
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}
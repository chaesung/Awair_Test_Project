package com.awair.testproject.event.detail.ui.component

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.awair.testproject.R
import com.awair.testproject.event.list.data.entity.Event

@Composable
fun EventDetailView(context: Context, event: Event) {
    Row {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(PaddingValues(vertical = 50.dp)),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(vertical = 10.dp)),
                text = event.title.takeUnless { it.isEmpty() }
                    ?: context.getString(R.string.no_title),
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(vertical = 10.dp)),
                text = "${context.getString(R.string.start_time)}: ${event.start}",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(vertical = 10.dp)),
                text = "${context.getString(R.string.end_time)}: ${event.end}",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )
        }
    }
}
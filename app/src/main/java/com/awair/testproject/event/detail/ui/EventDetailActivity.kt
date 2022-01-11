package com.awair.testproject.event.detail.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.awair.testproject.R
import com.awair.testproject.event.detail.ui.component.EventDetailView
import com.awair.testproject.event.list.data.entity.Event
import com.awair.testproject.event.list.ui.EventListFragment.Companion.EVENT_DETAIL

class EventDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getSerializableExtra(EVENT_DETAIL)?.run {
            val event = this as? Event
            event?.run {
                setContent {
                    EventDetailView(context = this@EventDetailActivity, event = this)
                }
            }
        }
    }
}
package com.awair.testproject.event.list.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awair.testproject.event.list.data.entity.Event
import com.awair.testproject.event.list.repository.EventListRepository
import com.awair.testproject.getDateFromFormat
import com.awair.testproject.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(private val mainRepository: EventListRepository) : ViewModel() {
    private val _networkResponse: MutableLiveData<NetworkResult<*>> =
        MutableLiveData<NetworkResult<*>>()
    val networkResponse: LiveData<NetworkResult<*>> = _networkResponse
    private val _toastMessage: MutableLiveData<Int> = MutableLiveData<Int>()
    val toastMessage: LiveData<Int> = _toastMessage
    private val eventList by lazy { arrayListOf<Event>() }
    val eventListWithConflictFlag by lazy { mutableStateListOf<Pair<Event, Boolean>>() }
    private var nextPageToken = ""

    /**
     * Get first page of event list when app starts
     */
    fun getEventList() {
        viewModelScope.launch {
            mainRepository.getEvents().collect {
                it.data?.run {
                    this@EventListViewModel.nextPageToken = nextPageToken
                    eventList.addAll(events)
                    sortEventsByStartEndDate()
                }
                _networkResponse.value = it
            }
        }
    }

    /**
     * Get next list of events using next page token
     */
    fun getNextEventList() {
        viewModelScope.launch {
            if (nextPageToken.isNotEmpty()) {
                mainRepository.getNextEvents(nextPageToken).collect {
                    it.data?.run {
                        this@EventListViewModel.nextPageToken = nextPageToken
                        eventList.addAll(events)
                        sortEventsByStartEndDate()
                    }
                    _networkResponse.value = it
                }
            } else {
                _toastMessage.value = SHOW_TOAST_NO_MORE_EVENT
            }
        }
    }

    /**
     * Retry to get the events
     * Triggered by user
     */
    fun retryGetEvent() {
        if (eventList.isEmpty())
            getEventList()
        else
            getNextEventList()
    }

    /**
     * Sorts event list by start and end date
     * Then checks the event list items for conflicts and recreates list with flag as pair
     */
    @SuppressLint("SimpleDateFormat")
    private fun sortEventsByStartEndDate() {
        eventList.sortBy { it.end.getDateFromFormat(DATE_FORMAT_STRING) }
        eventList.sortBy { it.start.getDateFromFormat(DATE_FORMAT_STRING) }
        eventListWithConflictFlag.clear()
        for(i in 0 until eventList.size - 1) {
            if(isOverlapping(eventList[i].start, eventList[i].end,
                    eventList[i+1].start, eventList[i+1].end)) {
                if(eventListWithConflictFlag.isNotEmpty())
                    eventListWithConflictFlag[i] = eventList[i] to true
                else
                    eventListWithConflictFlag.add(eventList[i] to true)
                eventListWithConflictFlag.add(eventList[i+1] to true)
            } else {
                if(eventListWithConflictFlag.isEmpty())
                    eventListWithConflictFlag.add(eventList[i] to false)
                eventListWithConflictFlag.add(eventList[i+1] to false)
            }
        }
    }

    /**
     * Utility method to check for date overlapping
     */
    private fun isOverlapping(start1: String, end1: String, start2: String, end2: String): Boolean {
        val start1Date = start1.getDateFromFormat(DATE_FORMAT_STRING)
        val end1Date = end1.getDateFromFormat(DATE_FORMAT_STRING)
        val start2Date = start2.getDateFromFormat(DATE_FORMAT_STRING)
        val end2Date = end2.getDateFromFormat(DATE_FORMAT_STRING)
        return start1Date.before(end2Date) && start2Date.before(end1Date)
    }

    companion object {
        const val SHOW_TOAST_NO_MORE_EVENT = 1000
        const val DATE_FORMAT_STRING = "MMMMM dd, yyyy h:mm aaa"
    }
}
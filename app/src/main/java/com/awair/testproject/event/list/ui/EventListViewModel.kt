package com.awair.testproject.event.list.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awair.testproject.event.list.data.entity.Event
import com.awair.testproject.event.list.data.entity.EventList
import com.awair.testproject.event.list.repository.EventListRepository
import com.awair.testproject.getDateFromFormat
import com.awair.testproject.getNewDateWithDiffFormat
import com.awair.testproject.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(private val mainRepository: EventListRepository) :
    ViewModel() {
    private val _networkResponse: MutableLiveData<NetworkResult<*>> =
        MutableLiveData<NetworkResult<*>>()
    val networkResponse: LiveData<NetworkResult<*>> = _networkResponse
    private val _toastMessage: MutableLiveData<Int> = MutableLiveData<Int>()
    val toastMessage: LiveData<Int> = _toastMessage
    private val eventList by lazy { arrayListOf<Event>() }
    val eventMapListWithConflictFlag by lazy { mutableStateMapOf<Long, List<Pair<Event, Boolean>>>() }
    private var nextPageToken = ""

    /**
     * Get first page of event list when app starts
     */
    fun getEventList() {
        viewModelScope.launch {
            mainRepository.getEvents()
                .collect(::handleGetEventResponse)
        }
    }

    /**
     * Get next list of events using next page token
     */
    fun getNextEventList() {
        viewModelScope.launch {
            if (nextPageToken.isNotEmpty()) {
                mainRepository.getNextEvents(nextPageToken)
                    .collect(::handleGetEventResponse)
            } else {
                _toastMessage.value = SHOW_TOAST_NO_MORE_EVENT
            }
        }
    }

    /**
     * Handle get event list api response
     * It updates next page token and adds new event items to list.
     * Finally calls sort function to sort the list and apply conflict flags
     * The run time complexity here would be n if api is successful other wise it would 1
     */
    private fun handleGetEventResponse(eventResponse: NetworkResult<EventList>) {
        eventResponse.data?.run {
            this@EventListViewModel.nextPageToken = nextPageToken
            eventList.addAll(events)
            sortEventsByStartEndDate()
        }
        _networkResponse.value = eventResponse
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
     * Because of the sorting logic the time complexity would be n log n
     */
    @SuppressLint("SimpleDateFormat")
    private fun sortEventsByStartEndDate() {
        eventList.sortBy { it.end.getDateFromFormat(DATE_FORMAT_STRING) }
        eventList.sortBy { it.start.getDateFromFormat(DATE_FORMAT_STRING) }
        val eventListWithConflictFlag = ArrayList<Pair<Event, Boolean>>()

        /**
         * After sorting is done it loops around sorted list
         * and creates new list with conflict flag included for every item
         * Since it's already sorted by start time and end time,
         * only comparison with next item is necessary to determine the conflict
         * Finally it will group the items by date and output as map by date
         */
        for (i in 0 until eventList.size - 1) {
            if (isOverlapping(
                    eventList[i].start, eventList[i].end,
                    eventList[i + 1].start, eventList[i + 1].end
                )
            ) {
                if (eventListWithConflictFlag.isNotEmpty())
                    eventListWithConflictFlag[i] = eventList[i] to true
                else
                    eventListWithConflictFlag.add(eventList[i] to true)
                eventListWithConflictFlag.add(eventList[i + 1] to true)
            } else {
                if (eventListWithConflictFlag.isEmpty())
                    eventListWithConflictFlag.add(eventList[i] to false)
                eventListWithConflictFlag.add(eventList[i + 1] to false)
            }
            eventMapListWithConflictFlag.clear()
            eventMapListWithConflictFlag.putAll(eventListWithConflictFlag.groupBy {
                it.first.start.getDateFromFormat(DATE_FORMAT_STRING)
                    ?.getNewDateWithDiffFormat(DATE_FORMAT_STRING_SECTION)?.time?: Date().time
            })
        }
    }

    /**
     * Utility method to check for date overlapping
     * If parse error occurs formatter will return null and it will set current date as default value
     */
    private fun isOverlapping(start1: String, end1: String, start2: String, end2: String): Boolean {
        val start1Date = start1.getDateFromFormat(DATE_FORMAT_STRING)?: Date()
        val end1Date = end1.getDateFromFormat(DATE_FORMAT_STRING)?: Date()
        val start2Date = start2.getDateFromFormat(DATE_FORMAT_STRING)?: Date()
        val end2Date = end2.getDateFromFormat(DATE_FORMAT_STRING)?: Date()
        return start1Date.before(end2Date) && start2Date.before(end1Date)
    }

    companion object {
        const val SHOW_TOAST_NO_MORE_EVENT = 1000
        const val DATE_FORMAT_STRING = "MMMMM d, yyyy h:mm aaa"
        const val DATE_FORMAT_STRING_SECTION = "MMMM d, yyyy"
    }
}
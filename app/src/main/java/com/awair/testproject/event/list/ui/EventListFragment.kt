package com.awair.testproject.event.list.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.awair.testproject.R
import com.awair.testproject.event.detail.ui.EventDetailActivity
import com.awair.testproject.event.list.data.entity.Event
import com.awair.testproject.event.list.ui.EventListViewModel.Companion.SHOW_TOAST_NO_MORE_EVENT
import com.awair.testproject.event.list.ui.component.EventListContent
import com.awair.testproject.network.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class EventListFragment : Fragment() {

    companion object {
        fun newInstance() = EventListFragment()
        const val BOTTOM_ITEM_VISIBILITY_BUFFER_COUNT = 2
        const val EVENT_DETAIL = "eventDetail"
    }

    private val viewModel: EventListViewModel by viewModels()
    private val isLoading = mutableStateOf(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getEventList()
        initObservers()
        return ComposeView(requireContext()).apply {
            setContent {
                EventListContent(
                    context = requireContext(),
                    eventStateList = viewModel.eventListWithConflictFlag,
                    isLoading = isLoading,
                    bottomReachedCallback = ::listBottomReached,
                    cardItemClick = ::listItemClicked
                )
            }
        }
    }

    private fun initObservers() {
        viewModel.networkResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    isLoading.value = false
                    showToast(getString(R.string.event_list_is_updated))
                }
                is NetworkResult.Error -> {
                    isLoading.value = false
                    showRetryAlertDialog()
                    showToast(getString(R.string.event_list_update_failed))
                }
                is NetworkResult.Loading -> isLoading.value = true
            }
        }
        viewModel.toastMessage.observe(viewLifecycleOwner) {
            val toastMessage = when (it) {
                SHOW_TOAST_NO_MORE_EVENT -> getString(R.string.no_more_events)
                else -> ""
            }
            showToast(toastMessage)
        }
    }

    private fun retryButtonClicked() {
        viewModel.retryGetEvent()
    }

    private fun listBottomReached() {
        viewModel.getNextEventList()
    }

    private fun listItemClicked(event: Event) {
        startActivity(Intent(requireContext(), EventDetailActivity::class.java).apply {
            putExtra(EVENT_DETAIL, event)
        })
    }

    private fun showRetryAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.event_list_update_failed))
            .setMessage(R.string.event_list_update_retry)
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                retryButtonClicked()
            }.create().show()
    }

    private fun showToast(message: String) {
        if (message.isNotEmpty())
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
package com.andriawan.testinglearning.utils.ext

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter

fun <T> CombinedLoadStates.decideOnState(
    adapter: T,
    showLoading: (isLoading: Boolean, isFirst: Boolean) -> Unit,
    showEmptyState: (Boolean) -> Unit,
    showError: (String) -> Unit
) {
    showLoading(append is LoadState.Loading, refresh is LoadState.Loading)

    showEmptyState(
        source.append is LoadState.NotLoading
                && source.append.endOfPaginationReached
                && (adapter as PagingDataAdapter<*, *>).itemCount == 0
    )

    val errorState = source.append as? LoadState.Error
        ?: source.prepend as? LoadState.Error
        ?: source.refresh as? LoadState.Error
        ?: append as? LoadState.Error
        ?: prepend as? LoadState.Error
        ?: refresh as? LoadState.Error

    errorState?.let {
        if ((adapter as PagingDataAdapter<*, *>).itemCount == 0) {
            showError(it.error.localizedMessage?.toString().toString())
        }
    }
}
package com.paylocity.ui.apod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paylocity.repository.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ApodViewModel(private val repository: ApodRepository) : ViewModel() {

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            repository.fetchApod()
        }
    }

    val viewState: Flow<ApodViewState> = repository.apodDataState.map {
        when (it) {
            is ApodFetchStateLoading -> ApodViewStateLoading(it.list)
            is ApodFetchStateEmpty -> ApodViewStateEmpty
            is ApodFetchStateError -> ApodViewStateError(it.list, it.error)
            is ApodFetchStateSuccess -> ApodViewStateSuccess(it.list)
        }
    }
}
package com.paylocity.ui.apod

import com.paylocity.repository.model.Apod

sealed class ApodViewState

data class ApodViewStateLoading(val list: List<Apod>) : ApodViewState()
data class ApodViewStateError(val list: List<Apod>, val error: String) : ApodViewState()
object ApodViewStateEmpty : ApodViewState()
data class ApodViewStateSuccess(val list: List<Apod>) : ApodViewState()

package com.strv.ui.apod

import com.strv.repository.model.Apod

sealed class ApodViewState

data class ApodViewStateLoading(val list: List<Apod>) : ApodViewState()
data class ApodViewStateError(val list: List<Apod>, val error: String) : ApodViewState()
object ApodViewStateEmpty : ApodViewState()
data class ApodViewStateSuccess(val list: List<Apod>) : ApodViewState()

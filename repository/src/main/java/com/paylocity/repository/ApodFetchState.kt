package com.paylocity.repository

import com.paylocity.repository.model.Apod

sealed class ApodFetchState

data class ApodFetchStateLoading(val list: List<Apod>) : ApodFetchState()
data class ApodFetchStateError(val list: List<Apod>, val error: String) : ApodFetchState()
object ApodFetchStateEmpty : ApodFetchState()
data class ApodFetchStateSuccess(val list: List<Apod>) : ApodFetchState()
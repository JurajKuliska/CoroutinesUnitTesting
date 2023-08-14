package com.paylocity.repository

import kotlinx.coroutines.flow.Flow

interface ApodRepository {

    suspend fun fetchApod()

    val apodDataState: Flow<ApodFetchState>
}
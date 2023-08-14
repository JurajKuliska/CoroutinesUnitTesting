package com.apod.repository

import kotlinx.coroutines.flow.Flow

interface ApodRepository {

    suspend fun fetchApod()

    val apodDataState: Flow<ApodFetchState>
}
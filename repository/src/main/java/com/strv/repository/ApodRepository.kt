package com.strv.repository

import kotlinx.coroutines.flow.Flow

interface ApodRepository {

    suspend fun fetchApod()

    val apodList: Flow<ApodFetchState>
}
package com.apod.persistence

import com.apod.persistence.model.ApodModel
import kotlinx.coroutines.flow.Flow

interface ApodPersistence {
    suspend fun updateData(list: List<ApodModel>)

    val apodList: Flow<List<ApodModel>>
}
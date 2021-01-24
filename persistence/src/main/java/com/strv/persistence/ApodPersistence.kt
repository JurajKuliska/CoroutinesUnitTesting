package com.strv.persistence

import com.strv.persistence.model.ApodModel
import kotlinx.coroutines.flow.Flow

interface ApodPersistence {
    suspend fun updateData(list: List<ApodModel>)

    suspend fun getAll(): Flow<List<ApodModel>>
}
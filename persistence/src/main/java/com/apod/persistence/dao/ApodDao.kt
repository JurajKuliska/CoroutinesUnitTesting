package com.apod.persistence.dao

import com.apod.persistence.model.ApodEntity
import kotlinx.coroutines.flow.Flow

internal interface ApodDao {
    suspend fun updateData(list: List<ApodEntity>)

    fun getAll(): Flow<List<ApodEntity>>
}
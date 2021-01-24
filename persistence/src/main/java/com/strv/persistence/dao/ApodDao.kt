package com.strv.persistence.dao

import com.strv.persistence.model.ApodEntity
import kotlinx.coroutines.flow.Flow

internal interface ApodDao {
    suspend fun updateData(list: List<ApodEntity>)

    fun getAll(): Flow<List<ApodEntity>>
}
package com.paylocity.persistence.dao

import com.paylocity.persistence.model.ApodEntity
import kotlinx.coroutines.flow.Flow

internal interface ApodDao {
    suspend fun updateData(list: List<ApodEntity>)

    fun getAll(): Flow<List<ApodEntity>>
}
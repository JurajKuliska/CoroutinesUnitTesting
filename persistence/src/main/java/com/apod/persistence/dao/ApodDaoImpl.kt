package com.apod.persistence.dao

import androidx.room.*
import com.apod.persistence.model.ApodEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ApodDaoImpl: ApodDao {
    @Query("SELECT * FROM apod")
    override fun getAll(): Flow<List<ApodEntity>>

    @Transaction
    override suspend fun updateData(list: List<ApodEntity>) {
        deleteAll()
        insertData(list)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(list: List<ApodEntity>)

    @Query("DELETE FROM apod")
    suspend fun deleteAll()
}
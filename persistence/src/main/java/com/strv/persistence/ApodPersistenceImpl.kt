package com.strv.persistence

import com.strv.persistence.dao.ApodDao
import com.strv.persistence.model.ApodModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@ExperimentalCoroutinesApi
internal class ApodPersistenceImpl(
    private val apodDao: ApodDao
) : ApodPersistence {

    override suspend fun updateData(list: List<ApodModel>) =
        apodDao.updateData(list.map { it.toApodEntity() })

    override val apodList: Flow<List<ApodModel>> =
        apodDao.getAll().flatMapLatest { list -> flowOf(list.map { it.toApodModel() }) }
}
package com.strv.persistence

import com.strv.persistence.dao.ApodDao

internal class ApodPersistenceImpl(
    private val apodDao: ApodDao
) : ApodPersistence {
}
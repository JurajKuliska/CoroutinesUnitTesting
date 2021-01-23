package com.strv.repository

import com.strv.api.ApodApi
import com.strv.persistence.ApodPersistence

internal class ApodRepositoryImpl(
    private val apodApi: ApodApi,
    private val apodPersistence: ApodPersistence
) : ApodRepository {
}
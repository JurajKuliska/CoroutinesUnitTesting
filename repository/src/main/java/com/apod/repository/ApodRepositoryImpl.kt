package com.apod.repository

import com.apod.api.ApodApi
import com.apod.api.RequestData
import com.apod.api.common.Response
import com.apod.api.dto.ApodDto
import com.apod.persistence.ApodPersistence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

internal class ApodRepositoryImpl(
    private val apodApi: ApodApi,
    private val apodPersistence: ApodPersistence
) : ApodRepository {

    private val loadingFlow = MutableStateFlow(false)
    private val errorFlow = MutableStateFlow<String?>(null)

    @Suppress("UNCHECKED_CAST")
    override suspend fun fetchApod() {
        loadingFlow.value = true
        errorFlow.value = null
        when (val result = apodApi.fetchApod(RequestData(count = 10))) {
            is Response.Error -> errorFlow.value = result.message
            is Response.Success<*> -> apodPersistence.updateData(
                (result.body as List<ApodDto>).map { it.toApodModel() }
            )
        }
        loadingFlow.value = false
    }

    override val apodDataState: Flow<ApodFetchState> =
        combine(
            apodPersistence.apodList,
            loadingFlow,
            errorFlow
        ) { data, isLoading, error ->
            when {
                isLoading ->
                    ApodFetchStateLoading(data.map { it.toApod() })
                error != null ->
                    ApodFetchStateError(data.map { it.toApod() }, error)
                data.isNotEmpty() && !isLoading ->
                    ApodFetchStateSuccess(data.map { it.toApod() })
                !isLoading ->
                    ApodFetchStateEmpty
                else -> throw IllegalStateException("unhandled state")
            }
        }
}
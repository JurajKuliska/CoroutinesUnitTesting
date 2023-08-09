package com.paylocity.persistence

import com.paylocity.persistence.model.ApodEntity
import com.paylocity.persistence.model.ApodModel

internal fun ApodEntity.toApodModel() =
    ApodModel(
        date = date,
        explanation = explanation,
        hdUrl = hdUrl,
        mediaType = mediaType,
        serviceVersion = serviceVersion,
        title = title,
        url = url
    )

internal fun ApodModel.toApodEntity() =
    ApodEntity(
        id = 0,
        date = date,
        explanation = explanation,
        hdUrl = hdUrl,
        mediaType = mediaType,
        serviceVersion = serviceVersion,
        title = title,
        url = url
    )
package com.apod.persistence

import com.apod.persistence.model.ApodEntity
import com.apod.persistence.model.ApodModel

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
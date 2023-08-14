package com.apod.repository

import com.apod.api.dto.ApodDto
import com.apod.persistence.model.ApodModel
import com.apod.repository.model.Apod

internal fun ApodDto.toApodModel() =
    ApodModel(
        date = date,
        explanation = explanation,
        hdUrl = hdUrl,
        mediaType = mediaType,
        serviceVersion = serviceVersion,
        title = title,
        url = url
    )

internal fun ApodModel.toApod() =
    Apod(
        date = date,
        explanation = explanation,
        hdUrl = hdUrl,
        mediaType = mediaType,
        serviceVersion = serviceVersion,
        title = title,
        url = url
    )
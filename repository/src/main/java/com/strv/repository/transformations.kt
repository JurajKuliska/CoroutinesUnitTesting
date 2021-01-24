package com.strv.repository

import com.strv.api.dto.ApodDto
import com.strv.persistence.model.ApodModel
import com.strv.repository.model.Apod

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
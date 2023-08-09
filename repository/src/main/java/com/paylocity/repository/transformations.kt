package com.paylocity.repository

import com.paylocity.api.dto.ApodDto
import com.paylocity.persistence.model.ApodModel
import com.paylocity.repository.model.Apod

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
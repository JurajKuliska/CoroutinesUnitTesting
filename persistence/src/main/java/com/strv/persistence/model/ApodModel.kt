package com.strv.persistence.model

import java.util.*

data class ApodModel(
    val date: Date,
    val explanation: String,
    val hdUrl: String?,
    val mediaType: String,
    val serviceVersion: String,
    val title: String,
    val url: String
)
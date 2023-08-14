package com.apod.repository.model

import java.util.*

data class Apod(
    val date: Date,
    val explanation: String,
    val hdUrl: String?,
    val mediaType: String,
    val serviceVersion: String,
    val title: String,
    val url: String
)
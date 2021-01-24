package com.strv.repository.model

import java.util.*

class Apod(
    val date: Date,
    val explanation: String,
    val hdUrl: String?,
    val mediaType: String,
    val serviceVersion: String,
    val title: String,
    val url: String
)
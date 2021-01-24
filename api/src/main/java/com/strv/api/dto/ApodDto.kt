package com.strv.api.dto

import com.strv.api.DateSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ApodDto constructor(
    @Serializable(with = DateSerializer::class)
    val date: Date,
    val explanation: String,
    @SerialName("hdurl") val hdUrl: String,
    @SerialName("media_type") val mediaType: String,
    @SerialName("service_version") val serviceVersion: String,
    val title: String,
    val url: String
)
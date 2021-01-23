@file:UseSerializers(com.strv.api.DateSerializer::class)
package com.strv.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ApodDto(
    val date: Date,
    val explanation: String,
    val hdUrl: String,
    @SerialName("media_type") val mediaType: String,
    @SerialName("service_version") val serviceVersion: String,
    val title: String,
    val url: String
)
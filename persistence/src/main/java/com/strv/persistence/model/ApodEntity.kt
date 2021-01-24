package com.strv.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "apod")
internal data class ApodEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "explanation") val explanation: String,
    @ColumnInfo(name = "hdUrl") val hdUrl: String?,
    @ColumnInfo(name = "media_type") val mediaType: String,
    @ColumnInfo(name = "service_version") val serviceVersion: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "url") val url: String
)
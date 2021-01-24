package com.strv.persistence.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.strv.persistence.model.ApodEntity

@Database(
    entities = [
        ApodEntity::class
    ],
    version = 2
)
@TypeConverters(DateConverter::class)
internal abstract class ApodDb : RoomDatabase() {
    abstract fun apodDao(): ApodDaoImpl

    companion object {
        @Volatile
        private var INSTANCE: ApodDb? = null

        fun getInstance(context: Context): ApodDb {
            val instance = INSTANCE
            if (instance != null) return instance
            synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context,
                    ApodDb::class.java,
                    "apod.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = newInstance
                return newInstance
            }
        }
    }
}
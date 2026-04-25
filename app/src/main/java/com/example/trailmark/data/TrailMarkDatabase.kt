package com.example.trailmark.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TrailMarker::class],
    version = 1,
    exportSchema = false
)
abstract class TrailMarkDatabase : RoomDatabase() {

    abstract fun trailMarkerDao(): TrailMarkerDao

    companion object {
        @Volatile
        private var INSTANCE: TrailMarkDatabase? = null

        fun getDatabase(context: Context): TrailMarkDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrailMarkDatabase::class.java,
                    "trailmark_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

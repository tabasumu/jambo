package com.mambobryan.jambo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [JamboLog::class], version = 1, exportSchema = false)
abstract class JamboLocalDB : RoomDatabase() {
    abstract fun jamboDao(): JamboLogDao

    companion object {
        @Volatile
        private var mInstance: JamboLocalDB? = null
        fun instance(context: Context): JamboLocalDB {
            if (mInstance == null) {
                mInstance = Room.databaseBuilder(
                    context.applicationContext,
                    JamboLocalDB::class.java,
                    context.applicationContext.packageName.plus("_jambo_db")
                ).build()

            }
            return mInstance!!
        }
    }
}
package com.mambobryan.jambo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "jamboLogTbl")
data class JamboLog(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "log_id")
    val id: Long = 0L,
    @ColumnInfo(name = "log_tag")
    val tag: String,
    @ColumnInfo(name = "log_package_name")
    val packageName: String,
    @ColumnInfo(name = "log_message")
    val message: String,
    @ColumnInfo(name = "log_timestamp")
    val timestamp: Long = Date().time,
    @ColumnInfo(name = "log_type")
    val type: LogType
)

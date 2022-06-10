package com.mambobryan.jambo.data

import androidx.paging.PagingSource
import androidx.room.*
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

@Dao
interface JamboLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg jamboLog: JamboLog)

    @Delete
    fun delete(vararg jamboLog: JamboLog)

    @Transaction
    @Query("DELETE FROM jamboLogTbl")
    fun deleteAll()

    @Transaction
    @Query("SELECT * FROM jamboLogTbl ORDER BY log_id ASC")
    fun allJamboLogs(): PagingSource<Int, JamboLog>

    @Transaction
    @Query("SELECT * FROM jamboLogTbl WHERE log_tag = :tag ORDER BY log_id ASC")
    fun filterJamboLogWithTag(tag: String): PagingSource<Int, JamboLog>

    @Transaction
    @Query("SELECT * FROM jamboLogTbl WHERE log_tag = :tag AND log_message LIKE '%' || :message || '%' ORDER BY log_id ASC")
    fun filterJamboLogWithTagAndMsg(tag: String, message: String): PagingSource<Int, JamboLog>

    @Transaction
    @Query("SELECT * FROM jamboLogTbl WHERE log_message LIKE '%' || :message || '%' ORDER BY log_id ASC")
    fun searchJamboLogs(message: String): PagingSource<Int, JamboLog>


}


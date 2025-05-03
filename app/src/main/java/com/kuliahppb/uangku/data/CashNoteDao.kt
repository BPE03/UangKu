package com.kuliahppb.uangku.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.kuliahppb.uangku.model.CashNote

@Dao
interface CashNoteDao {
    @Query("SELECT * FROM CashNote ORDER BY id DESC")
    fun getAll(): Flow<List<CashNote>>

    @Insert
    suspend fun insert(note: CashNote)

    @Delete
    suspend fun delete(note: CashNote)
}

//@Dao
//interface CashNoteDao {
//    @Query("SELECT * FROM CashNote ORDER BY id DESC")
//    fun getAll(): List<CashNote>
//
//    @Insert
//    suspend fun insert(note: CashNote)
//
//    @Delete
//    fun delete(note: CashNote)
//}
package com.kuliahppb.uangku.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuliahppb.uangku.model.CashNote

@Database(entities = [CashNote::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cashNoteDao(): CashNoteDao
}
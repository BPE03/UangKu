package com.kuliahppb.uangku.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "cashnote-db"
            ).build().also { INSTANCE = it }
        }
    }

//    val db = Room.databaseBuilder(
//        context = applicationContext,
//        AppDatabase::class.java, "database-name"
//    ).build()
}
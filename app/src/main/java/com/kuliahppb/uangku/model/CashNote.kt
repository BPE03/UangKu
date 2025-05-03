package com.kuliahppb.uangku.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDateTime(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(Date())
}

@Entity
data class CashNote(
    @PrimaryKey val id: Long,
    val amount: String,
    val type: String,
    val description: String,
    val dateAdded: String = getCurrentDateTime()
)
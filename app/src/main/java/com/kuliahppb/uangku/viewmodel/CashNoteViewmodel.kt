package com.kuliahppb.uangku.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.kuliahppb.uangku.model.CashNote
import com.kuliahppb.uangku.data.DatabaseProvider
import com.kuliahppb.uangku.model.getCurrentDateTime
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CashNoteViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = DatabaseProvider.getDatabase(application).cashNoteDao()
//    val allNotes: LiveData<List<CashNote>> = dao.getAll().asLiveData()
    val allNotes: StateFlow<List<CashNote>> = dao.getAll()
    .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val totalIncome: StateFlow<Int> = dao.getAll()
        .map { notes ->
            notes.filter { it.type == "Pemasukan/Income" }
                .sumOf { it.amount.toIntOrNull() ?: 0 }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val totalOutcome: StateFlow<Int> = dao.getAll()
        .map { notes ->
            notes.filter { it.type == "Pengeluaran/Outcome" }
                .sumOf { it.amount.toIntOrNull() ?: 0 }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    fun addNote(amount: String, type: String, description: String) {
        viewModelScope.launch {
            val note = CashNote(
                id = System.currentTimeMillis(),
                amount = amount,
                type = type,
                description = description,
                dateAdded = getCurrentDateTime()
            )
            dao.insert(note)
        }
    }

    fun deleteNote(note: CashNote) {
        viewModelScope.launch {
            dao.delete(note)
        }
    }
//    val totalIncome: LiveData<Int> = Transformations.map(allNotes) { notes ->
//        notes.filter { it.type == "Pemasukan/Income" }
//            .sumOf { it.amount.toIntOrNull() ?: 0 }
//    }
//
//    val totalOutcome: LiveData<Int> = Transformations.map(allNotes) { notes ->
//        notes.filter { it.type == "Pengeluaran/Outcome" }
//            .sumOf { it.amount.toIntOrNull() ?: 0 }
//    }
}
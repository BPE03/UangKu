package com.kuliahppb.uangku

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuliahppb.uangku.ui.theme.UangKuTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuliahppb.uangku.model.CashNote
import com.kuliahppb.uangku.viewmodel.CashNoteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UangKuTheme {
                UangKuApp()
            }
        }
    }
}

@Composable
fun UangKuApp(viewModel: CashNoteViewModel = viewModel()) {
    val radioOptions = listOf("Pemasukan/Income", "Pengeluaran/Outcome")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    var inputMoney by remember { mutableStateOf("") }
    var noteDescription by remember { mutableStateOf("") }
//    val notes by viewModel.allNotes.observeAsState(emptyList())
//    val totalIncome by viewModel.totalIncome.observeAsState(0)
//    val totalOutcome by viewModel.totalOutcome.observeAsState(0)
    val notes by viewModel.allNotes.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalOutcome by viewModel.totalOutcome.collectAsState()
    val balance = totalIncome - totalOutcome
    var showDialog by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<CashNote?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "UangKu",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("Ringkasan", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("Total Income: Rp $totalIncome", color = Color.Green)
        Text("Total Outcome: Rp $totalOutcome", color = Color.Red)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Total Balance: Rp $balance",
            color = if (totalOutcome > totalIncome) Color.Red else Color.Green
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("Buat Nota Baru:")
        OutlinedTextField(
            value = inputMoney, onValueChange = { inputMoney = it },
            label = { Text("Jumlah Uang") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Jenis Nota:",
            fontSize = 14.sp,
//            fontWeight = FontWeight.Bold
        )
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null // null recommended for accessibility with screen readers
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = noteDescription, onValueChange = { noteDescription = it },
            label = { Text("Deskripsi nota uang") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (inputMoney.isNotBlank() && noteDescription.isNotBlank()) {
                viewModel.addNote(
                    inputMoney, selectedOption, noteDescription
                )
                inputMoney = ""
                noteDescription = ""
            } else {
                Toast.makeText(context, "Pastikan Jumlah Uang / Deskripsi tidak kosong", Toast.LENGTH_SHORT).show()
            }

        }) {
            Text("Simpan Nota")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Riwayat Nota", fontWeight = FontWeight.Bold)

        LazyColumn {
            items(notes) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("${note.type}: Rp ${note.amount}")
                            Text("Deskripsi: ${note.description}")
                            Text("Tanggal: ${note.dateAdded}", style = MaterialTheme.typography.bodySmall)
                        }

                        IconButton(onClick = {
                            noteToDelete = note
                            showDialog = true
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus")
                        }
                    }
                }
            }
        }
    }
    if (showDialog && noteToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus nota ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteNote(noteToDelete!!)
                        showDialog = false
                    }
                ) {
                    Text("Hapus", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
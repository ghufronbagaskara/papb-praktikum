package com.example.papbpraktikum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.papbpraktikum.data.model.Note
import com.example.papbpraktikum.ui.viewmodel.NoteViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        noteViewModel.fetchNotes()
        setContent {
            NotesScreen(noteViewModel)
        }
    }
}

@Composable
fun NotesScreen(noteViewModel: NoteViewModel) {
    val notes by noteViewModel.notes.observeAsState(emptyList())
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Tambah Catatan Baru",
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Judul") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Isi Catatan") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()){
                    noteViewModel.addNote(title, content)
                    title = ""
                    content = ""
                }
            }
        ) {
            Text("Simpan Catatan")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Daftar Catatan", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(notes) { note ->
                NoteItem(note)
            }
        }
    }
}

@Composable
fun NoteItem(note: Note) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
package com.example.papbpraktikum.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.papbpraktikum.data.model.Note
import com.google.firebase.firestore.FirebaseFirestore

class NoteViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    fun fetchNotes() {
        db.collection("notes")
            .get()
            .addOnSuccessListener { result ->
                val noteList = result.map { document ->
                    Note(
                        id = document.id,
                        title = document.getString("title") ?: "",
                        content = document.getString("content") ?: "",
                    )
                }
                _notes.value = noteList
            }
    }

    fun addNote(title: String, content: String) {
        val note = hashMapOf(
            "title" to title,
            "content" to content
        )

        db.collection("notes")
            .add(note)
            .addOnSuccessListener {
                fetchNotes()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun deleteNote(id: String) {
        db.collection("notes").document(id)
            .delete()
            .addOnSuccessListener {
                fetchNotes()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun udpateNote(id: String, title: String, content: String) {
        db.collection("notes").document(id)
            .update(
                mapOf(
                    "title" to title,
                    "content" to content
                )
            )
            .addOnSuccessListener {
                fetchNotes()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
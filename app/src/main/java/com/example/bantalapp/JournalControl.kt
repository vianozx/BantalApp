package com.example.bantalapp

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

class JournalControl {
    private val db = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    private val currentDateAndTime = Timestamp.now()

    fun tambah(judul:String, isi:String){
        val currentUser = auth.currentUser!!.uid
        val journal = hashMapOf(
            "NoteTitle" to judul,
            "NoteContent" to isi,
            "uid" to currentUser,
            "time" to currentDateAndTime
            )
        db.collection("Journal").add(journal)
    }
}
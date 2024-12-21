package com.example.bantalapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class ViewJournalActivity : AppCompatActivity() {
    private lateinit var tvJudul :TextView
    private lateinit var tvIsi :TextView
    private  lateinit var isi:String
    private  var judul:String ="Loading......"
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_journal)
        tvJudul = findViewById(R.id.tvJudulView)
        tvIsi = findViewById(R.id.tvContentView)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val journalTitle = intent.getStringExtra("journalTitle")
        val documentId = intent.getStringExtra("documentId")

        if (documentId != null) {
            firestore.collection("Journal").document(documentId).get().addOnSuccessListener { document ->
                    judul = document.getString("NoteTitle")?:"error"
                    isi = document.getString("NoteContent")?:"error"
                    tvIsi.text = isi
                    tvJudul.text=judul



            }
        }

        // Use the documentId to fetch more details or do further processing
        Log.d("ViewJournalActivity", "Received document ID: $documentId")
    }
}
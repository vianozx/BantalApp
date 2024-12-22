package com.example.bantalapp

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class meditasi : AppCompatActivity() {
    private lateinit var tvJudul : TextView
    private lateinit var tvIsi : TextView
    private var firestore :FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_meditasi)
        tvJudul = findViewById(R.id.tvJudulAktivitas)
        tvIsi = findViewById(R.id.tvIsiAktivitas)
        var judul = intent.getStringExtra("journalTitle")
        val documentId = intent.getStringExtra("documentId")


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (documentId != null) {
            firestore.collection("aktivitas").document(documentId).get()
                .addOnSuccessListener { document ->
                    judul = document.getString("nama") ?: "error"
                    var isi :String = document.getString("langkah") ?: "error"
                    isi.replace("\\n","\n")
                    tvIsi.text = isi
                    tvJudul.text = judul


                }
        }
    }
}
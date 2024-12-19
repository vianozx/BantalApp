package com.example.bantalapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class addJournal : AppCompatActivity() {
    private lateinit var inputJudul : EditText
    private lateinit var inputIsi : EditText
    private lateinit var backBtn:ImageButton
    private lateinit var saveBtn:FloatingActionButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firestore: FirebaseFirestore
    private lateinit var journal: JournalControl

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_journal)
        backBtn = findViewById(R.id.backBtn)
        saveBtn = findViewById(R.id.btnSave)
        inputJudul = findViewById(R.id.inputJudul)
        inputIsi = findViewById(R.id.inputIsi)
        journal = JournalControl()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        backBtn.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        saveBtn.setOnClickListener(View.OnClickListener {
            val title : String = inputJudul.text.toString()
            val content : String = inputIsi.text.toString()
            if (title.isEmpty() || content.isEmpty()){
                Toast.makeText(
                    applicationContext,
                    "Judul atau isi Tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else{
                journal.tambah(title,content)
            }
        })


    }
}
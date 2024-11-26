package com.example.bantalapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import com.google.firebase.auth.FirebaseUser

class SignUp : AppCompatActivity() {
    private lateinit var emailInputDaftar :EditText
    private lateinit var passInputDaftar :EditText;
    private lateinit var bDaftar:Button
    private lateinit var bKembali:Button
    private lateinit var firebaseAuth: FirebaseAuth
    private val TAG = "signUp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        emailInputDaftar = findViewById<EditText>(R.id.emailInputDaftar)
        passInputDaftar = findViewById<EditText>(R.id.passInputDaftar)
        bDaftar = findViewById<Button>(R.id.bDaftar)
        bKembali = findViewById<Button>(R.id.bKembali)
        firebaseAuth = FirebaseAuth.getInstance()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bDaftar.setOnClickListener(View.OnClickListener() {
            var email: String = emailInputDaftar.text.toString().trim()
            var pass: String = passInputDaftar.text.toString().trim()
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "email atau password tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this){task ->
                    if (task.isComplete){
                        Toast.makeText(
                            applicationContext,
                            "Berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG,"createUserWithEmail:success")
                        startActivity(Intent(this, MainActivity::class.java))
                    }else{
                        Log.d(TAG, "createUserWithEmail: failure", task.exception)
                        Toast.makeText(
                            applicationContext,
                            "Gak Berhasil :(",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
        bKembali.setOnClickListener(View.OnClickListener() {
            startActivity(Intent(this, MainActivity::class.java)) })
    }
    fun sendEmailVerification(){
        var user = firebaseAuth.currentUser
        if (user != null){
            user.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(
                        applicationContext,
                        "Berhasil kirim email",
                        Toast.LENGTH_SHORT
                    ).show()
                    firebaseAuth.signOut()
                    finish()
                }
            }
        }
    }
}
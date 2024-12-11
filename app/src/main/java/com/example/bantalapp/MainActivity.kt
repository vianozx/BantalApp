package com.example.bantalapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bantalapp.HomePage
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var emailInput:EditText
    private lateinit var passInput:EditText
    private lateinit var firebaseAuth:FirebaseAuth
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        emailInput = findViewById(R.id.emailInput)
        passInput = findViewById(R.id.passInput)
        firebaseAuth = FirebaseAuth.getInstance()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun masuk(view: View) {
        var email :String = emailInput.text.toString().trim()
        var pass :String = passInput.text.toString().trim()
        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(applicationContext, "email atau password tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = firebaseAuth.currentUser
                    startActivity(Intent(this, HomePage::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }

        }
    }
    fun daftar(view:View){
        startActivity(Intent(this, SignUp::class.java))
    }
}

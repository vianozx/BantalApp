package com.example.bantalapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bantalapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class InputData : AppCompatActivity() {
    private var db:FirebaseFirestore=FirebaseFirestore.getInstance()
    private var auth:FirebaseAuth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data) // Sesuaikan dengan nama layout XML Anda

        // Referensi elemen-elemen di layout
        val editTextFullName: EditText = findViewById(R.id.editTextFullName)
        val editTextTanggalLahir: EditText = findViewById(R.id.tanggalLahir)
        val radioGroupGender: RadioGroup = findViewById(R.id.radioGroupGender)
        val checkBoxYes: CheckBox = findViewById(R.id.checkYes)
        val buttonSave: Button = findViewById(R.id.buttonSave)

        // Menampilkan DatePickerDialog saat EditText diklik
        editTextTanggalLahir.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Format tanggal yang dipilih ke dalam EditText
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    editTextTanggalLahir.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // Handle tombol "Selanjutnya"
        buttonSave.setOnClickListener {
            val fullName = editTextFullName.text.toString()
            val tanggalLahir = editTextTanggalLahir.text.toString()

            // Mendapatkan jenis kelamin yang dipilih
            val selectedGenderId = radioGroupGender.checkedRadioButtonId
            val gender = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                "Belum dipilih"
            }

            // Status penggunaan obat
            val isUsingMedicine = checkBoxYes.isChecked
            val userid= auth.currentUser?.uid

            // Tampilkan data yang diinput (atau simpan ke database/layar berikutnya)
            val message = """
                Nama Lengkap: $fullName
                Tanggal Lahir: $tanggalLahir
                Jenis Kelamin: $gender
                Penggunaan Obat: ${if (isUsingMedicine) "Ya" else "Tidak"}
            """.trimIndent()

            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            val currentUser = auth.currentUser!!.uid
            val user = hashMapOf(
                "namaLengkap" to fullName,
                "tanggalLahir" to tanggalLahir,
                "jenisKelamin" to gender,
                "penggunaanObat" to isUsingMedicine,
                "uid" to userid
            )
            db.collection("Users").add(user)
            startActivity(Intent(this, HomePage::class.java))
        }

    }
}

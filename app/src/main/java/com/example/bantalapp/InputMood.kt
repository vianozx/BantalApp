package com.example.bantalapp
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bantalapp.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import java.util.Calendar

class InputMood : AppCompatActivity() {
    private lateinit var radioGroup: RadioGroup
    private lateinit var spinner3: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var editText: EditText
    private lateinit var saveButton: Button
    private val auth:FirebaseAuth = FirebaseAuth.getInstance()
    private val db :FirebaseFirestore = FirebaseFirestore.getInstance()
    private val currentDateAndTime = Timestamp.now()
    private val date = Calendar.DATE
    private val month = Calendar.DATE
    private val year = Calendar.DATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_mood)
        // Initialize spinners by finding them by ID
        spinner2 = findViewById(R.id.jawaban2)
        spinner3 = findViewById(R.id.jawaban3)
        radioGroup = findViewById(R.id.radioGroup)
        editText = findViewById(R.id.jawaban4)
        saveButton = findViewById(R.id.button)
        // Create an adapter for the spinner using a string array from resources
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.numbers_array, // This refers to the array of numbers 1-10 from strings.xml
            android.R.layout.simple_spinner_item
        )
        val adapter2 = ArrayAdapter.createFromResource(
            this,
            R.array.numbers_array2, // This refers to the array of numbers 1-10 from strings.xml
            android.R.layout.simple_spinner_item
        )

        // Set the dropdown layout style for the spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the spinners
        spinner2.adapter = adapter
        spinner3.adapter = adapter2

        saveButton.setOnClickListener {
            // Get selected radio button text
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            val mood = findViewById<RadioButton>(selectedRadioButtonId)?.text.toString()

            // Get spinner values
            val kekuatanEmosi = spinner2.selectedItem.toString()
            val sleepHours = spinner3.selectedItem.toString()

            // Get edit text value
            val feelingDescription = editText.text.toString()

            // Log or save data
            Log.d("MoodTracker", "Mood: $mood, Sleep1: $kekuatanEmosi, Sleep2: $sleepHours, Description: $feelingDescription")
            saveMood(mood,kekuatanEmosi,sleepHours,feelingDescription )
            val intent = Intent(this, HomePage::class.java)
            intent.putExtra("FRAGMENT_NAME", "MoodFragment") // Pass the fragment to load
            startActivity(intent)
        }
    }
    fun saveMood(mood:String, kuatEmosi:String, tidur :String, penyebab:String){
        val currentUser = auth.currentUser!!.uid
        val timestamp =currentDateAndTime
        val mood = hashMapOf(
            "uid" to currentUser,
            "mood" to mood,
            "kuatEmosi" to kuatEmosi,
            "tidur" to tidur,
            "penyebab" to penyebab,
            "timeStamp" to timestamp
        )
        db.collection("Mood").add(mood)
    }
}

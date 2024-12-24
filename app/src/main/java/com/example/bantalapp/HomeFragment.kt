package com.example.bantalapp

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeFragment : Fragment() {
    private lateinit var tvJudulHome: TextView
    private lateinit var tvIsiHome: TextView
    private lateinit var tvNama: TextView
    private val auth:FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var pieChart: PieChart

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var rvMood: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize views
        tvJudulHome = view.findViewById(R.id.tvJudulHome)
        tvIsiHome = view.findViewById(R.id.tvIsiHome)
        tvNama = view.findViewById(R.id.tvNama)
        isiNama()
        // Fetch the most recent journal data
        fetchMostRecentJournal()

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize spinners by finding them by ID
        pieChart = view.findViewById(R.id.pieChart)
        setupPieChart()

        // Load data using coroutines
        lifecycleScope.launch {
            loadPieChartData()
        }
    }
    private suspend fun getMoodCount(uid: String, mood: String? = null): Int {
        var query = firestore.collection("Mood").whereEqualTo("uid", uid)
        if (mood != null) {
            query = query.whereEqualTo("mood", mood)
        }else {
            query = query
        }
        return try {
            val snapshot = query.count().get(AggregateSource.SERVER).await()
            snapshot.count.toInt()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error fetching mood count: $mood", e)
            0
        }
    }
    private fun setupPieChart() {
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)
        pieChart.setCenterText("Mood Distribution")
        pieChart.setCenterTextSize(20f)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = true
        pieChart.setUsePercentValues(true)
    }
    private fun isiNama(){
        val user = auth.currentUser?.uid
        firestore.collection("Users").whereEqualTo("uid",user).get().addOnSuccessListener { documents ->
            for (document in documents){
                tvNama.text = document.getString("namaLengkap")
            }
        }
    }
    private suspend fun loadPieChartData() {
        val user = auth.currentUser?.uid ?: return

        // Fetch counts for each mood
        val total = getMoodCount(user)
        if (total == 0) {
            Log.e("FirestoreError", "No moods recorded.")
            return
        }

        val sangatSenang = getMoodCount(user, "Sangat Senang")
        val senang = getMoodCount(user, "Senang")
        val biasaSaja = getMoodCount(user, "Biasa Saja")
        val murung = getMoodCount(user, "Murung")
        val sedih = getMoodCount(user, "Sedih")

        // Calculate percentages
        val entries = listOf(
            PieEntry((sangatSenang.toFloat() / total), "Sangat Senang"),
            PieEntry((senang.toFloat() / total) , "Senang"),
            PieEntry((biasaSaja.toFloat() / total) , "Biasa Saja"),
            PieEntry((murung.toFloat() / total), "Murung"),
            PieEntry((sedih.toFloat() / total) , "Sedih")
        )

        // Create and set PieData
        val dataSet = PieDataSet(entries, "Mood Distribution")
        dataSet.colors = listOf(
            Color.rgb(33, 150, 243),
            Color.rgb(76, 175, 80),
            Color.rgb(255, 193, 7),
            Color.rgb(156, 39, 176),
            Color.rgb(244, 67, 54)
        )
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 16f

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.invalidate() // Refresh chart
    }

    private fun fetchMostRecentJournal() {
        val user = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("Journal").whereEqualTo("uid",user)// Replace with your collection name
            .orderBy("timestamp", Query.Direction.DESCENDING) // Sort by timestamp in descending order
            .limit(1) // Get only the most recent document
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val title = document.getString("NoteTitle") ?: "No Title"
                    val content = document.getString("NoteContent") ?: "No Content"

                    // Set the data to TextViews
                    tvJudulHome.text = title
                    tvIsiHome.text = content
                } else {
                    tvJudulHome.text = "No Journal Found"
                    tvIsiHome.text = ""
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching journal: ", e)
                tvJudulHome.text = "Error"
                tvIsiHome.text = "Could not fetch journal."
            }
    }
}

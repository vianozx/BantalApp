package com.example.bantalapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

class MoodFragment : Fragment(),MoodAdapter.OnMoodClickListener {
    private lateinit var pieChart: PieChart
    private val auth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var rvMood: RecyclerView
    private lateinit var adapter: MoodAdapter
    private lateinit var inputMoodBtn:Button
    private val moodItems = ArrayList<Mood>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mood, container, false)
        rvMood = view.findViewById(R.id.rvMood)
        inputMoodBtn = view.findViewById(R.id.inputMoodBtn)
        inputMoodBtn.setOnClickListener{
            val intent = Intent(requireContext(), InputMood::class.java)
            startActivity(intent)
        }

        adapter = MoodAdapter(moodItems, this)
        rvMood.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)


        rvMood.adapter = adapter

        loadMood()
        return view

    }
    override fun onResume() {
        super.onResume()
        // Load data whenever the fragment becomes visible
        loadMood()
    }
    private fun loadMood() {
        val user = auth.currentUser?.uid
        firestore.collection("Mood")
            .whereEqualTo("uid", user)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    error.printStackTrace()
                    return@addSnapshotListener
                }

                val newItems = ArrayList<Mood>()

                snapshots?.let {
                    for (document in it.documents) {
                        val mood = document.getString("mood") ?: ""
                        val kuatEmosi = document.getString("kuatEmosi")?:""
                        val timeStamp = document.getTimestamp("timeStamp")?.toDate()?: ""
                        val timestampString = formatTimestampToString(timeStamp)
                        val tidur = document.getString("tidur")?:""
                        val penyebab = document.getString("penyebab")?:""
                        val documentId = document.id // Get the document ID

                        newItems.add(Mood(mood,timestampString,kuatEmosi,tidur,penyebab, documentId))
                    }

                    // Sort the journals by timestamp
                    val sortedItems = newItems.sortedBy { it.timestamp }

                    // Update the adapter with sorted data
                    adapter.updateData(sortedItems)
                }
            }
    }
    fun formatTimestampToString(timestamp: Serializable): String {
        val date = timestamp // Convert timestamp to Date object
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Define your desired format
        return format.format(date) // Convert date to string
    }

    override fun onMoodClick(position: Int) {
        val clickedMood = moodItems[position]
        val intent = Intent(requireContext(), ViewMoodActivity::class.java)
        intent.putExtra("mood", clickedMood.mood)
        intent.putExtra("documentId", clickedMood.documentId) // Pass document ID
        startActivity(intent)
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
}

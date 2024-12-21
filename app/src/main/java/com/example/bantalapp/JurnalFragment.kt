package com.example.bantalapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bantalapp.R.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class JurnalFragment : Fragment(), OnJournalClickListener {
    private lateinit var btnAddNote: FloatingActionButton
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val journalTitles = ArrayList<String>()
    private val journalContent = ArrayList<String>()
    private val time = ArrayList<String>()
    private lateinit var rvJournal: RecyclerView
    private lateinit var adapter: JournalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jurnal, container, false)
        btnAddNote = view.findViewById(R.id.btnNote)
        rvJournal = view.findViewById(R.id.rvJournal)

        btnAddNote.setOnClickListener {
            val intent = Intent(requireContext(), addJournal::class.java)
            startActivity(intent)
        }

        // Initialize the adapter with the click listener
        adapter = JournalAdapter(journalTitles, journalContent, time, this)
        rvJournal.layoutManager = GridLayoutManager(requireContext(), 2)
        rvJournal.adapter = adapter

        // Fetch data from Firestore
        loadImportantJournals()

        return view
    }

    override fun onResume() {
        super.onResume()
        // Load data whenever the fragment becomes visible
        loadImportantJournals()
    }

    private fun loadImportantJournals() {
        val user = auth.currentUser?.uid
        firestore.collection("Journal")
            .whereEqualTo("uid", user)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    error.printStackTrace()
                    return@addSnapshotListener
                }

                val newTitles = ArrayList<String>()
                val newContents = ArrayList<String>()
                val newTimes = ArrayList<String>()

                // Clear the existing data in the lists
                newTitles.clear()
                newContents.clear()
                newTimes.clear()

                snapshots?.let {
                    for (document in it.documents) {
                        document.getString("NoteTitle")?.let { newTitles.add(it) }
                        document.getString("NoteContent")?.let { newContents.add(it) }
                        document.getTimestamp("timestamp")?.toDate()?.toString()?.let { newTimes.add(it) }
                    }

                    // Sort the journals by timestamp
                    val sortedIndices = newTimes.indices.sortedBy { newTimes[it] }
                    val sortedTitles = sortedIndices.map { newTitles[it] }
                    val sortedContents = sortedIndices.map { newContents[it] }
                    val sortedTimes = sortedIndices.map { newTimes[it] }

                    // Update the adapter with sorted data
                    adapter.updateData(sortedTitles, sortedContents, sortedTimes)
                }
            }
    }

    override fun onJournalClick(position: Int) {
        // Handle the click event
        Log.d("JournalClick", "Item clicked at position $position")

        // Example: Navigate to a detailed activity or fragment
        val clickedJournal = journalTitles[position]
        val intent = Intent(requireContext(), ViewJournalActivity::class.java)
        intent.putExtra("journalTitle", clickedJournal)
        startActivity(intent)
    }
}

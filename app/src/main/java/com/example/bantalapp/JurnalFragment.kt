package com.example.bantalapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bantalapp.R.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class JurnalFragment : Fragment(), JournalAdapter.OnJournalClickListener {
    private lateinit var btnAddNote: FloatingActionButton
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val journalItems = ArrayList<Journal>() // Use a list of JournalItem
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
        adapter = JournalAdapter(journalItems, this) // Pass journalItems list to the adapter
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

                val newItems = ArrayList<Journal>()

                snapshots?.let {
                    for (document in it.documents) {
                        val title = document.getString("NoteTitle") ?: ""
                        val content = document.getString("NoteContent") ?: ""
                        val timestamp = document.getTimestamp("timestamp")?.toDate()?.toString() ?: ""
                        val documentId = document.id // Get the document ID

                        newItems.add(Journal( content,title, timestamp, documentId))
                    }

                    // Sort the journals by timestamp
                    val sortedItems = newItems.sortedBy { it.timestamp }

                    // Update the adapter with sorted data
                    adapter.updateData(sortedItems)
                }
            }
    }

    override fun onJournalClick(position: Int) {
        // Handle the click event
        Log.d("JournalClick", "Item clicked at position $position")

        // Get the clicked journal item
        val clickedJournal = journalItems[position]

        // Pass both title and document ID to the next activity
        val intent = Intent(requireContext(), ViewJournalActivity::class.java)
        intent.putExtra("journalTitle", clickedJournal.NoteTitle)
        intent.putExtra("documentId", clickedJournal.documentId) // Pass document ID
        startActivity(intent)
    }
}

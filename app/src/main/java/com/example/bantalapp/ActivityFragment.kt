package com.example.bantalapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bantalapp.ProdukAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ActivityFragment : Fragment(), ProdukAdapter.OnAktivitasClickListener {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val aktivitasItem = ArrayList<aktivitas>() // Use a list of JournalItem
    private lateinit var rvProduk: RecyclerView

    lateinit var adapterProduk: ProdukAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_activity, container, false)

        // Bind RecyclerView with findViewById
        rvProduk = view.findViewById(R.id.rvJournal)

        rvProduk.layoutManager = LinearLayoutManager(requireContext())

        adapterProduk = ProdukAdapter(aktivitasItem,this)
        rvProduk.adapter = adapterProduk
        loadAktivitas()
        return view
    }
    private fun loadAktivitas() {
        firestore.collection("Journal")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    error.printStackTrace()
                    return@addSnapshotListener
                }

                val newItems = ArrayList<aktivitas>()

                snapshots?.let {
                    for (document in it.documents) {
                        val nama = document.getString("nama") ?: ""
                        val deskripsi = document.getString("deskripsi") ?: ""
                        val langkah = document.getString("langkah") ?: ""
                        val documentId = document.id.toString()
                        newItems.add(aktivitas( nama,deskripsi,langkah,documentId))
                    }



                    // Update the adapter with sorted data
                    adapterProduk.updateData(newItems)
                }
            }
    }

    override fun onAktivitasClick(position: Int) {
        val clickedJournal = aktivitasItem[position]

        // Pass both title and document ID to the next activity
        val intent = Intent(requireContext(), meditasi::class.java)
        intent.putExtra("nama", clickedJournal.nama)
        intent.putExtra("documentId", clickedJournal.documentId) // Pass document ID
        startActivity(intent)
    }
}

package com.example.bantalapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class JurnalFragment : Fragment() {
    lateinit var rvProduk: RecyclerView
    var namaProduk = ArrayList<String>()
    var hargaProduk = ArrayList<String>()
    var bgJournal = ArrayList<Int>()
    lateinit var btnNote : FloatingActionButton

    lateinit var journaladapter: JournalAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jurnal, container, false)

        // Initialize RecyclerView
        rvProduk = view.findViewById(R.id.rvProduk)
        btnNote = view.findViewById(R.id.btnNote)
        rvProduk.layoutManager = GridLayoutManager(context,2)
        namaProduk.add("Susu")
        namaProduk.add("Kopi")
        namaProduk.add("Teh")

        hargaProduk.add("10.000")
        hargaProduk.add("9.000")
        hargaProduk.add("5.000")
        namaProduk.add("Susu")
        namaProduk.add("Kopi")
        namaProduk.add("Teh")

        hargaProduk.add("10.000")
        hargaProduk.add("9.000")
        hargaProduk.add("5.000")
        namaProduk.add("Susu")
        namaProduk.add("Kopi")
        namaProduk.add("Teh")

        hargaProduk.add("10.000")
        hargaProduk.add("9.000")
        hargaProduk.add("5.000")

        bgJournal.add(R.drawable.sticky1)
        bgJournal.add(R.drawable.sticky2)
        bgJournal.add(R.drawable.sticky3)

        journaladapter = JournalAdapter(namaProduk, hargaProduk, bgJournal, requireContext())
        rvProduk.adapter = journaladapter
        btnNote.setOnClickListener {
            val intent = Intent(requireContext(), addJournal::class.java)
            startActivity(intent)
        }
        return view
    }
}
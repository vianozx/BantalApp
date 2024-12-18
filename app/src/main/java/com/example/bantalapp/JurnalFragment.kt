package com.example.bantalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class JurnalFragment : Fragment() {
    lateinit var rvProduk: RecyclerView
    var namaProduk = ArrayList<String>()
    var hargaProduk = ArrayList<String>()
    var bgJournal = ArrayList<Int>()

    lateinit var journaladapter: JournalAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jurnal, container, false)

        // Initialize RecyclerView
        rvProduk = view.findViewById(R.id.rvProduk)

        rvProduk.layoutManager = GridLayoutManager(context,2)
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

        return view
    }
}
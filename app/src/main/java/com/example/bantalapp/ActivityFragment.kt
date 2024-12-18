package com.example.bantalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contohrv2.ProdukAdapter

class ActivityFragment : Fragment() {

    lateinit var rvProduk: RecyclerView
    var namaProduk = ArrayList<String>()
    var hargaProduk = ArrayList<String>()
    var gambarProduk = ArrayList<Int>()
    var deskripsi = ArrayList<String>()

    lateinit var adapterProduk: ProdukAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_activity, container, false)

        // Bind RecyclerView with findViewById
        rvProduk = view.findViewById(R.id.rvProduk)

        rvProduk.layoutManager = LinearLayoutManager(requireContext())
        namaProduk.add("Meditasi")
        namaProduk.add("Olahraga")
        namaProduk.add("Teh")

        hargaProduk.add("Ayo lakukan Meditasi sekarang!")
        hargaProduk.add("Jika dirimu ")
        hargaProduk.add("5.000")

        gambarProduk.add(R.drawable.susu)
        gambarProduk.add(R.drawable.kopi)
        gambarProduk.add(R.drawable.teh_tawar)
        deskripsi.add("Test Ini untuk deskripsi")
        deskripsi.add("Test Ini untuk deskripsi")
        deskripsi.add("Test Ini untuk deskripsi")

        adapterProduk = ProdukAdapter(namaProduk, hargaProduk, gambarProduk, requireContext(),  deskripsi)
        rvProduk.adapter = adapterProduk

        return view
    }
}

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
        rvProduk = view.findViewById(R.id.rvJournal)

        rvProduk.layoutManager = LinearLayoutManager(requireContext())
        namaProduk.add("Meditasi")
        namaProduk.add("Olahraga")
        namaProduk.add("Relaksasi")
        namaProduk.add("Sosialisasi")
        namaProduk.add("Hobi")

        hargaProduk.add("klik disini!")
        hargaProduk.add("klik disini!")
        hargaProduk.add("klik disini!")
        hargaProduk.add("klik disini!")
        hargaProduk.add("klik disini!")

        gambarProduk.add(R.drawable.meditasi)
        gambarProduk.add(R.drawable.olahraga)
        gambarProduk.add(R.drawable.relaksasi)
        gambarProduk.add(R.drawable.sosialisasi)
        gambarProduk.add(R.drawable.hobi)


        deskripsi.add("\"Dengan melakukan meditasi akan membuat hati menjadi lebih tenang\"")
        deskripsi.add("\"Dengan melakukan olahraga rutin akan membuat badan menjadi lebih segar dan bugar\"")
        deskripsi.add("\"Dengan istirahat yang cukup dapat membuat pikiran menjadi tenang\"")
        deskripsi.add("\"Dengan mengikuti sosialisasi yang ada akan menambah banyak wawasan\"")
        deskripsi.add("\"Melakukan hobi yang biasa kamu lakukan dengan senang hati akan membuatmu membalikkan moodmu\"")

        adapterProduk = ProdukAdapter(namaProduk, hargaProduk, gambarProduk, requireContext(),  deskripsi)
        rvProduk.adapter = adapterProduk

        return view
    }
}

package com.example.bantalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bantalapp.R

class ProdukAdapter (
    var namaProduk: ArrayList<String>,
    var hargaProduk: ArrayList<String>,
    var gambarProduk: ArrayList<Int>,
    var context: Context,
    var deskripsi: ArrayList<String>
): RecyclerView.Adapter<ProdukAdapter.CountryViewHolder>() {

    class CountryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var tvNamaProduk: TextView = itemView.findViewById(R.id.tvJudul)
        var tvDeskripsi: TextView = itemView.findViewById(R.id.tvDeskripsi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.produk_card, parent, false)
        return CountryViewHolder(view)
    }

    override fun getItemCount(): Int {

        return namaProduk.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        holder.tvNamaProduk.text = namaProduk.get(position)
        holder.tvDeskripsi.text = deskripsi.get(position)
    }


}
package com.example.bantalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JournalAdapter(
    var namaProduk: ArrayList<String>, // Corrected to ArrayList<String>
    var hargaProduk: ArrayList<String>, // Already correct
    var bgJournal: ArrayList<Int>, // Corrected to ArrayList<Int>
    var context: Context // Moved this to the last position
) : RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

    class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNamaProduk: TextView = itemView.findViewById(R.id.tvNamaProduk)
        var tvHargaProduk: TextView = itemView.findViewById(R.id.tvHargaProduk)
        var imageView: ImageView = itemView.findViewById(R.id.bgJournal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.journal_card, parent, false)
        return JournalViewHolder(view)
    }

    override fun getItemCount(): Int {
        return namaProduk.size // Now this works because namaProduk is an ArrayList<String>
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        holder.tvNamaProduk.text = namaProduk[position]
        holder.tvHargaProduk.text = hargaProduk[position]
        holder.imageView.setImageResource(bgJournal[position]) // Works because gambarProduk is ArrayList<Int>
    }
}
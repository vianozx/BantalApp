package com.example.bantalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bantalapp.JournalAdapter.OnJournalClickListener
import com.example.bantalapp.R

class ProdukAdapter (
    private var aktivitasItem: ArrayList<aktivitas>,
    private val listener: OnAktivitasClickListener
) : RecyclerView.Adapter<ProdukAdapter.aktivitasViewHolder>() {
    interface OnAktivitasClickListener {
        fun onAktivitasClick(position: Int)
    }
    inner class aktivitasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvjudul: TextView = itemView.findViewById(R.id.tvJudul)
        var tvDeskripsi: TextView = itemView.findViewById(R.id.tvDeskripsi)


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onAktivitasClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): aktivitasViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.produk_card, parent, false)
        return aktivitasViewHolder(view)
    }



    override fun onBindViewHolder(holder: aktivitasViewHolder, position: Int) {
        val aktivitas = aktivitasItem[position]
        holder.tvjudul.text = aktivitas.nama
        holder.tvDeskripsi.text = aktivitas.deskripsi
    }

    fun updateData(newItems: List<aktivitas>) {
        aktivitasItem.clear()
        aktivitasItem.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return aktivitasItem.size
    }
}
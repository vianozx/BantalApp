package com.example.bantalapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JournalAdapter(
    var noteTitle: ArrayList<String>,
    var noteContent: ArrayList<String>,
    var time: ArrayList<String>,
    private val listener: OnJournalClickListener // Add the listener as a parameter
) : RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

    inner class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvnoteTitle: TextView = itemView.findViewById(R.id.noteTitle)
        var noteDesc: TextView = itemView.findViewById(R.id.noteDesc)
        var noteDesc2: TextView = itemView.findViewById(R.id.noteDesc2)

        init {
            itemView.setOnClickListener(this) // Set the click listener on the itemView
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onJournalClick(position) // Trigger the click listener
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.journal_card, parent, false)
        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        holder.tvnoteTitle.text = noteTitle[position]
        holder.noteDesc.text = noteContent[position]
        holder.noteDesc2.text = time[position]
    }

    fun updateData(newTitles: List<String>, newContents: List<String>, newTimes: List<String>) {
        noteTitle.clear()
        noteTitle.addAll(newTitles)

        noteContent.clear()
        noteContent.addAll(newContents)

        time.clear()
        time.addAll(newTimes)

        notifyDataSetChanged() // Notify adapter of data change
    }

    override fun getItemCount(): Int {
        return minOf(noteTitle.size, noteContent.size, time.size)
    }
}

// Define the interface for handling item click events
interface OnJournalClickListener {
    fun onJournalClick(position: Int)
}

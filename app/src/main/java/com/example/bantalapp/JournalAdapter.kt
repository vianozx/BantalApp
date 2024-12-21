package com.example.bantalapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JournalAdapter(
    private var journalItems: ArrayList<Journal>,
    private val listener: OnJournalClickListener
) : RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {
    interface OnJournalClickListener {
        fun onJournalClick(position: Int)
    }
    inner class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvnoteTitle: TextView = itemView.findViewById(R.id.noteTitle)
        var noteDesc: TextView = itemView.findViewById(R.id.noteDesc)
        var noteDesc2: TextView = itemView.findViewById(R.id.noteDesc2)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onJournalClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.journal_card, parent, false)
        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val journal = journalItems[position]
        holder.tvnoteTitle.text = journal.NoteTitle
        holder.noteDesc.text = journal.NoteContent
        holder.noteDesc2.text = journal.timestamp
    }

    fun updateData(newItems: List<Journal>) {
        journalItems.clear()
        journalItems.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return journalItems.size
    }
}


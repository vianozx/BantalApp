package com.example.bantalapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

class MoodAdapter (
    private var moodItems:ArrayList<Mood>,
    private val listener:OnMoodClickListener
    ): RecyclerView.Adapter<MoodAdapter.MoodViewHolder>(){
    interface OnMoodClickListener {
        fun onMoodClick(position: Int)
    }
    inner class MoodViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener{
        val moodLayout: LinearLayout = itemView.findViewById(R.id.moodLayout)
        var tvMood : TextView = itemView.findViewById(R.id.tvMood)
        var tvTimestamp:TextView = itemView.findViewById(R.id.tvTimeStamp)
        var tvPenyebab:TextView = itemView.findViewById(R.id.tvPenyebab)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemView.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mood_card, parent,false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val mood = moodItems[position]
        holder.tvMood.text = mood.mood
        holder.tvPenyebab.text = mood.penyebab
        holder.tvTimestamp.text = mood.timestamp
        if (mood.mood == "Sangat Senang"){
            ViewCompat.setBackgroundTintList(holder.moodLayout, ContextCompat.getColorStateList(holder.moodLayout.context, R.color.neon_yellow))
        }
        else if(mood.mood =="Senang"){
            ViewCompat.setBackgroundTintList(holder.moodLayout, ContextCompat.getColorStateList(holder.moodLayout.context, R.color.neon_green))
        }
        else if(mood.mood =="Biasa Saja"){
            ViewCompat.setBackgroundTintList(holder.moodLayout, ContextCompat.getColorStateList(holder.moodLayout.context, R.color.neon_purple))
        }
        else if(mood.mood =="Sedih"){
            ViewCompat.setBackgroundTintList(holder.moodLayout, ContextCompat.getColorStateList(holder.moodLayout.context, R.color.neon_blue))
        }
        else if(mood.mood =="Murung"){
            ViewCompat.setBackgroundTintList(holder.moodLayout, ContextCompat.getColorStateList(holder.moodLayout.context, R.color.neon_red))
        }
    }

    fun updateData(newItems:List<Mood>){
        moodItems.clear()
        moodItems.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return moodItems.size
    }


}
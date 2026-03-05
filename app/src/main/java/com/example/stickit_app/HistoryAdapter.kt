package com.example.stickit_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val listHistory: List<HistoryModel>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    // Class ViewHolder buat pegang semua ID dari XML item_history
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvSubtitle: TextView = view.findViewById(R.id.tvSubtitle)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val ivItemIcon: ImageView = view.findViewById(R.id.ivItemIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false) // Pastikan file XML-nya bernama item_history.xml
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listHistory[position]

        holder.tvTitle.text = data.title
        holder.tvSubtitle.text = data.subtitle
        holder.tvPrice.text = data.price
        holder.ivItemIcon.setImageResource(data.image)
    }

    override fun getItemCount(): Int = listHistory.size
}
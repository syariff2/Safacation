package com.sawelo.safacation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DetailReviewAdapter(private val reviewData: List<Pair<String, String>>) :
    RecyclerView.Adapter<DetailReviewAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaReviewer: TextView = itemView.findViewById(R.id.nama_reviewer)
        val kontenReviewer: TextView = itemView.findViewById(R.id.konten_reviewer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_detail_review, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (nama, konten) = reviewData[position]
        holder.namaReviewer.text = nama
        holder.kontenReviewer.text = konten
    }

    override fun getItemCount(): Int = reviewData.size
}
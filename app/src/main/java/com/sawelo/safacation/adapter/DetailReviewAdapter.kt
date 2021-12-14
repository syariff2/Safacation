package com.sawelo.safacation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sawelo.safacation.R
import com.sawelo.safacation.data.ReviewLokasi

class DetailReviewAdapter(private val reviewData: List<ReviewLokasi>?) :
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
        if (reviewData != null) {
            val nama = reviewData[position].namaReviewer
            val konten = reviewData[position].kontenReviewer
            holder.namaReviewer.text = nama
            holder.kontenReviewer.text = konten
        }
    }

    override fun getItemCount(): Int = reviewData?.size ?: 0
}
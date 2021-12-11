package com.sawelo.safacation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterSafa(private val listSafa : List<DataSafa>) : RecyclerView.Adapter<AdapterSafa.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_location, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (namaLokasi, alamat, poster, deskripsi ) = listSafa [position]
        holder.dataname.text = namaLokasi
        holder.dataaddress.text = alamat
        holder.dataposter.setImageResource(poster)
        holder.datadeskripsi.text = deskripsi

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listSafa[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listSafa.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dataname: TextView = itemView.findViewById(R.id.namelokasi)
        var dataaddress: TextView = itemView.findViewById(R.id.addresslokasi)
        var dataposter: ImageView = itemView.findViewById(R.id.posterlokasi)
        var datadeskripsi: TextView = itemView.findViewById(R.id.deskripsilokasi)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataSafa)
    }
}
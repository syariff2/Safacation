package com.sawelo.safacation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sawelo.safacation.R
import com.sawelo.safacation.data.DataSafa

class AdapterSafa(private val listSafa: List<DataSafa>) :
    RecyclerView.Adapter<AdapterSafa.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ListViewHolder {
        val view: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.list_location, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        fun gambarResId(name: String): Int {
            val view = holder.itemView
            return view.resources.getIdentifier(name, "drawable", view.context.packageName)
        }

        val dataSafa = listSafa[position]
        holder.dataname.text = dataSafa.nama
        holder.dataaddress.text = dataSafa.alamat
        holder.dataposter.setImageResource(gambarResId(dataSafa.gambar[0]))
        holder.datadeskripsi.text = dataSafa.deskripsi

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(dataSafa.nama)
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
        fun onItemClicked(namaLokasi: String)
    }
}
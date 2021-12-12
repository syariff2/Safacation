package com.sawelo.safacation.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sawelo.safacation.R
import com.sawelo.safacation.activity.DetailActivity
import com.sawelo.safacation.adapter.AdapterSafa
import com.sawelo.safacation.data.DataSafa
import com.sawelo.safacation.data.SourceData

class LocationFragment : Fragment(), AdapterSafa.OnItemClickCallback {

    private lateinit var rvLocation: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvLocation = view.findViewById(R.id.rv_location)
        rvLocation.setHasFixedSize(true)

        showRecyclerList()
    }

    override fun onItemClicked(data: DataSafa) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.DETAIL_EXTRA, data)
        startActivity(intent)
    }

    private fun showRecyclerList() {
        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvLocation.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            rvLocation.layoutManager = LinearLayoutManager(requireContext())
        }

        val dataSafa = mutableListOf<DataSafa>()
        for (i in SourceData.nameLokasi.indices) {
            dataSafa.add(DataSafa(
                SourceData.nameLokasi[i],
                SourceData.alamat[i],
                SourceData.gambarLokasi[i][0],
                SourceData.deskripsi[i]
            ))
        }

        val locationAdapter = AdapterSafa(dataSafa)
        locationAdapter.setOnItemClickCallback(this)
        rvLocation.adapter = locationAdapter
    }
}
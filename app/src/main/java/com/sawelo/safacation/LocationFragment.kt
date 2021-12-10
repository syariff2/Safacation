package com.sawelo.safacation

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
import java.util.*

class LocationFragment : Fragment(), AdapterSafa.OnItemClickCallback {

    private lateinit var rvLocation: RecyclerView
    private val locationList = ArrayList<DataSafa>()

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

        locationList.addAll(dataList)
        showRecyclerList()
    }

    override fun onItemClicked(data: DataSafa) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.DETAIL_EXTRA, data)
        startActivity(intent)
    }

    private val dataList: ArrayList<DataSafa>
        get() {
            val dataLokasi = resources.getStringArray(R.array.name_lokasi)
            val dataAddress = resources.getStringArray(R.array.address)
            val dataPoster = resources.obtainTypedArray(R.array.data_poster)
            val dataDeskripsi = resources.getStringArray(R.array.deskripsi_lokasi)
            val listSafa = ArrayList<DataSafa>()

            for (i in dataLokasi.indices) {
                val posterItemResId = dataPoster.getResourceId(i, -1)
                val posterItemArray = resources.obtainTypedArray(posterItemResId)

                val data = DataSafa(
                    dataLokasi[i],
                    dataAddress[i],
                    posterItemArray.getResourceId(0, 0),
                    dataDeskripsi[i]
                )
                posterItemArray.recycle()
                listSafa.add(data)
            }

            dataPoster.recycle()
            return listSafa
        }

    private fun showRecyclerList() {
        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvLocation.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            rvLocation.layoutManager = LinearLayoutManager(requireContext())
        }
        val locationAdapter = AdapterSafa(locationList)
        locationAdapter.setOnItemClickCallback(this)
        rvLocation.adapter = locationAdapter
    }
}
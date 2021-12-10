package com.sawelo.safacation

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sawelo.safacation.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvlocation: RecyclerView
    private val listloca = ArrayList<DataSafa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvlocation = findViewById(R.id.rv_location)
        rvlocation.setHasFixedSize(true)

        listloca.addAll(dataList)
        showRecyclerList()

        if (savedInstanceState == null) {
            loadFragment(LocationFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.location_tab -> loadFragment(LocationFragment())
                R.id.health_tab -> loadFragment(HealthFragment())
            }
            true
        }
    }

    private val dataList: ArrayList<DataSafa>
        get() {
            val datalokasi = resources.getStringArray(R.array.name_lokasi)
            val dataaddress = resources.getStringArray(R.array.address)
            val dataposter = resources.obtainTypedArray(R.array.data_poster)
            val datadeskripsi = resources.getStringArray(R.array.deskripsi_lokasi)
            val listSafa = ArrayList<DataSafa>()
            for (i in datalokasi.indices) {
                val dataloca = DataSafa(datalokasi[i],dataaddress[i], dataposter.getResourceId(i, -1),datadeskripsi[i])
                listSafa.add(dataloca)
            }
            return listSafa
        }

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvlocation.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvlocation.layoutManager = LinearLayoutManager(this)
        }
        val listLocalSafa = AdapterSafa(listloca)
        rvlocation.adapter = listLocalSafa
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, fragment)
        }
    }
}
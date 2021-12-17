package com.sawelo.safacation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.sawelo.safacation.R
import com.sawelo.safacation.databinding.ActivityMainBinding
import com.sawelo.safacation.fragment.HealthFragment
import com.sawelo.safacation.fragment.LocationFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        Toast.makeText(this,"Welcome", Toast.LENGTH_SHORT).show()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, fragment)
        }
    }
}
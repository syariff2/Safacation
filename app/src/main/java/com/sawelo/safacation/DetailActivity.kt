package com.sawelo.safacation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.sawelo.safacation.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val photoList = listOf(
            R.drawable.asiafarm,
            R.drawable.asiafarm1,
            R.drawable.asiafarm2
        )

        binding.detailPhotoPager.adapter = DetailPhotoAdapter(
            supportFragmentManager, lifecycle, photoList,
        )

        val initialPosition = "1 / ${DetailPhotoAdapter.ITEM_COUNT}"
        binding.detailPhotoPosition.text = initialPosition

        binding.detailPhotoPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    when {
                        (positionOffset < 0.5) -> {
                            val currentPosition = "${position + 1} / ${DetailPhotoAdapter.ITEM_COUNT}"
                            binding.detailPhotoPosition.text = currentPosition
                        }
                        (positionOffset >= 0.5) -> {
                            val currentPosition = "${position + 2} / ${DetailPhotoAdapter.ITEM_COUNT}"
                            binding.detailPhotoPosition.text = currentPosition
                        }
                    }
                }
            }
        )
    }

    /**
     * Jika tombol kembali ditekan, maka kembali ke MainActivity
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }
}
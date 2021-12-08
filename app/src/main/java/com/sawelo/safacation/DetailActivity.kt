package com.sawelo.safacation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.sawelo.safacation.databinding.ActivityDetailBinding
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.detail_recycler_view)
        recyclerView.setHasFixedSize(true)

        val namaLokasi = intent.extras?.getString(NAMA_LOKASI_EXTRA)
        val dataPosition = DetailSourceData.nameLokasi.indexOf(namaLokasi)

        val alamatLokasi = DetailSourceData.alamat[dataPosition]
        val jadwalBuka = DetailSourceData.jadwalBuka[dataPosition]
        val dataReview = DetailSourceData.dataReview[dataPosition]
        val dataGambar = DetailSourceData.gambarLokasi[dataPosition]
        val dataBintang = DetailSourceData.bintang[dataPosition]

        setDetailPhoto(dataGambar)
        binding.detailNamaLokasi.text = namaLokasi
        binding.detailAlamat.text = alamatLokasi
        binding.detailJadwalBuka.text = jadwalBuka

        binding.detailRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = DetailReviewAdapter(dataReview)
        }

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

    private fun setDetailPhoto(dataGambar: IntArray) {
        binding.detailPhotoPager.adapter = DetailPhotoAdapter(
            supportFragmentManager, lifecycle, dataGambar,
        )

        val initialPhotoPosition = "1 / ${DetailPhotoAdapter.ITEM_COUNT}"
        binding.detailPhotoPosition.text = initialPhotoPosition

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

    fun Double.mRound(value: Double, factor: Double): Double {
        return (value / factor).roundToInt() * factor
    }

    companion object {
        const val NAMA_LOKASI_EXTRA = "NAMA_LOKASI_EXTRA"
    }
}
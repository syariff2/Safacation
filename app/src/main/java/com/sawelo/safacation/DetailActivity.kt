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

        val detailExtra = intent.extras?.getParcelable<DataSafa>(DETAIL_EXTRA)
        val detailExtraNama = detailExtra?.namelokasi

        val dataPosition = DetailSourceData.nameLokasi.indexOf(detailExtraNama)

        val alamatLokasi = DetailSourceData.alamat[dataPosition]
        val jadwalBuka = DetailSourceData.jadwalBuka[dataPosition]
        val dataReview = DetailSourceData.dataReview[dataPosition]
        val dataGambar = DetailSourceData.gambarLokasi[dataPosition]
        val dataBintang = DetailSourceData.bintang[dataPosition]

        val gambarBintang = when (dataBintang.mRound(0.5)) {
            0.5 -> R.drawable.star_review_0_5
            1.0 -> R.drawable.star_review_1_0
            1.5 -> R.drawable.star_review_1_5
            2.0 -> R.drawable.star_review_2_0
            2.5 -> R.drawable.star_review_2_5
            3.0 -> R.drawable.star_review_3_0
            3.5 -> R.drawable.star_review_3_5
            4.0 -> R.drawable.star_review_4_0
            4.5 -> R.drawable.star_review_4_5
            5.0 -> R.drawable.star_review_5_0
            else -> R.drawable.star_review_0_5
        }

        setDetailPhoto(dataGambar)
        binding.detailNamaLokasi.text = detailExtraNama
        binding.detailAlamat.text = alamatLokasi
        binding.detailJadwalBuka.text = jadwalBuka
        binding.detailBintang.setImageResource(gambarBintang)

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

    private fun Double.mRound(factor: Double): Double {
        return (this / factor).roundToInt() * factor
    }

    companion object {
        const val DETAIL_EXTRA = "DETAIL_EXTRA"
    }
}
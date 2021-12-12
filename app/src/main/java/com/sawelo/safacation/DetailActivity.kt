package com.sawelo.safacation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sawelo.safacation.databinding.ActivityDetailBinding
import com.sawelo.safacation.utils.ResourcesValuePull
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var mMap: GoogleMap

    private lateinit var namaLokasi: String
    private lateinit var alamatLokasi: String
    private lateinit var jadwalBuka: String
    private lateinit var dataPosterResult: List<Int>
    private lateinit var dataReviewResult: List<Pair<String, String>>

    private var dataBintang: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.detail_recycler_view)
        recyclerView.setHasFixedSize(true)

        val detailExtra = intent.extras?.getParcelable<DataSafa>(DETAIL_EXTRA)
        val detailExtraNama = detailExtra?.namaLokasi

        getDataFromResource(detailExtraNama)

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

        setDetailPhoto(dataPosterResult)
        binding.detailNamaLokasi.text = namaLokasi
        binding.detailAlamat.text = alamatLokasi
        binding.detailJadwalBuka.text = jadwalBuka
        binding.detailBintang.setImageResource(gambarBintang)

        binding.detailRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = DetailReviewAdapter(dataReviewResult)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.detail_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isScrollGesturesEnabled = false

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(0.535664, 101.443361)
        mMap.addMarker(MarkerOptions().position(sydney).title(namaLokasi))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15F))
    }

    private fun getDataFromResource(detailExtraNama: String?) {
        val dataPositionInResource =
            if (detailExtraNama != null) {
                resources.getStringArray(R.array.nama_lokasi).indexOf(detailExtraNama)
            } else 0

        val resourcesValue = ResourcesValuePull(resources)

        namaLokasi = resourcesValue.getNamaLokasi(dataPositionInResource)
        alamatLokasi = resourcesValue.getAlamatLokasi(dataPositionInResource)
        jadwalBuka = resourcesValue.getJadwalBuka(dataPositionInResource)
        dataBintang = resourcesValue.getDataBintang(dataPositionInResource)
        dataReviewResult = resourcesValue.getDataReview(dataPositionInResource)
        dataPosterResult = resourcesValue.getDataPoster(dataPositionInResource)
    }

    private fun setDetailPhoto(dataGambar: List<Int>) {
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
                            val currentPosition =
                                "${position + 1} / ${DetailPhotoAdapter.ITEM_COUNT}"
                            binding.detailPhotoPosition.text = currentPosition
                        }
                        (positionOffset >= 0.5) -> {
                            val currentPosition =
                                "${position + 2} / ${DetailPhotoAdapter.ITEM_COUNT}"
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
package com.sawelo.safacation.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.sawelo.safacation.R
import com.sawelo.safacation.SafaApplication
import com.sawelo.safacation.adapter.DetailPhotoAdapter
import com.sawelo.safacation.adapter.DetailReviewAdapter
import com.sawelo.safacation.data.DataSafa
import com.sawelo.safacation.data.room.DataSafaDao
import com.sawelo.safacation.databinding.ActivityDetailBinding
import com.sawelo.safacation.viewmodel.DetailViewModel
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var mMap: GoogleMap
    private lateinit var dao: DataSafaDao

    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dao = SafaApplication.databaseInstance.DataSafaDao()
        recyclerView = findViewById(R.id.detail_recycler_view)
        recyclerView.setHasFixedSize(true)

        val detailExtra = intent.extras?.getString(DETAIL_EXTRA)

        if (detailExtra != null) {
            setInformation(detailExtra)
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
        mMap.uiSettings.setAllGesturesEnabled(false)

        /**
         * Mencari lokasi dalam database Google API
         * Jika pencaharian gagal maka akan tampil Snackbar sebagai info
         */
        detailViewModel.findPlace { onFailure ->
            binding.detailMap.isVisible = false
            Log.e(DetailViewModel.TAG, "findPlaceFails: $onFailure")
            Snackbar.make(binding.detailScrollView, "Failed to connect with Google Maps", Snackbar.LENGTH_SHORT)
                .show()
        }

        /**
         * Mengawasi koordinat dari lokasi yang sudah dicari
         * Jika berubah maka tambah marker/penanda dalam peta
         * dan posisikan kamera peta sesuai lokasi
         */
        detailViewModel.placeCoordinate.observe(this, { latLng ->
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(detailViewModel.namaLokasi.value)
                    .alpha(0.7F)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
        })
    }

    private fun setInformation(namaLokasi: String) {

        fun gambarBintang(double: Double) = when (double.mRound(0.5)) {
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

        detailViewModel.setNamaLokasi(namaLokasi)
        val dataSafa: LiveData<DataSafa> = Transformations.switchMap(detailViewModel.namaLokasi) {
            dao.getLokasi(it)
        }

        dataSafa.observe(this, {
            setDetailPhoto(it.gambar)
            binding.detailNamaLokasi.text = it.nama
            binding.detailAlamat.text = it.alamat
            binding.detailJadwalBuka.text = it.jadwalBuka
            binding.detailBintang.setImageResource(gambarBintang(it.bintang))

            binding.detailRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@DetailActivity)
                adapter = DetailReviewAdapter(it.review)
            }
        })

        /**
         * Mengawasi nilai id lokasi dari Google API
         * Jika berubah maka tambahkan daftar review dalam database
         */
        detailViewModel.idLokasi.observe(this, { idLokasi ->
            detailViewModel.getReview(idLokasi) {
                val tempDataSafa = dataSafa.value
                if (tempDataSafa != null) {
                    tempDataSafa.review.addAll(it)
                    SafaApplication.executorService.execute {
                        dao.insertAll(tempDataSafa)
                    }
                }
            }
        })

        detailViewModel.isLoading.observe(this, {
            binding.detailLoading.isVisible = it
        })
    }

    private fun setDetailPhoto(dataGambar: List<String>?) {
        val daftarGambar = mutableListOf<Int>()
        dataGambar?.forEach {
            val resId = resources.getIdentifier(
                it, "drawable", packageName
            )
            daftarGambar.add(resId)
        }

        binding.detailPhotoPager.adapter = DetailPhotoAdapter(
            supportFragmentManager, lifecycle, daftarGambar,
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
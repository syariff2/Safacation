package com.sawelo.safacation.activity

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
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.sawelo.safacation.BuildConfig
import com.sawelo.safacation.R
import com.sawelo.safacation.adapter.DetailPhotoAdapter
import com.sawelo.safacation.adapter.DetailReviewAdapter
import com.sawelo.safacation.data.DataSafa
import com.sawelo.safacation.data.SourceData
import com.sawelo.safacation.databinding.ActivityDetailBinding
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var mMap: GoogleMap

    private lateinit var namaLokasi: String
    private lateinit var alamatLokasi: String
    private lateinit var jadwalBuka: String
    private lateinit var dataPoster: List<Int>
    private lateinit var dataReview: List<Pair<String, String>>

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

        setDetailPhoto(dataPoster)
        binding.detailNamaLokasi.text = namaLokasi
        binding.detailAlamat.text = alamatLokasi
        binding.detailJadwalBuka.text = jadwalBuka
        binding.detailBintang.setImageResource(gambarBintang)

        binding.detailRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = DetailReviewAdapter(dataReview)
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

        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        val placesClient = Places.createClient(this)
        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder().apply {
            sessionToken = token
            query = namaLokasi
        }.build()
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { predictionResponse ->
                val prediction = predictionResponse.autocompletePredictions[0]
                val placeId = prediction.placeId
                val placeFields = listOf(Place.Field.LAT_LNG)
                val detailRequest = FetchPlaceRequest.newInstance(placeId, placeFields)
                placesClient.fetchPlace(detailRequest)
                    .addOnSuccessListener { placeResponse ->
                        val latLng = placeResponse.place.latLng ?: LatLng(0.0,0.0)
                        mMap.addMarker(MarkerOptions()
                            .position(latLng)
                            .title(namaLokasi)
                            .alpha(0.7F))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
                    }
            }
    }

    private fun getDataFromResource(detailExtraNama: String?) {
        val dataPositionInResource =
            if (detailExtraNama != null) {
                SourceData.nameLokasi.indexOf(detailExtraNama)
            } else 0

        namaLokasi = SourceData.nameLokasi[dataPositionInResource]
        alamatLokasi = SourceData.alamat[dataPositionInResource]
        jadwalBuka = SourceData.jadwalBuka[dataPositionInResource]
        dataBintang = SourceData.bintang[dataPositionInResource]
        dataReview = SourceData.dataReview[dataPositionInResource]
        dataPoster = SourceData.gambarLokasi[dataPositionInResource]
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
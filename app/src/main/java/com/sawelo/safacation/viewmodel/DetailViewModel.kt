package com.sawelo.safacation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.sawelo.safacation.BuildConfig
import com.sawelo.safacation.data.ReviewLokasi
import com.sawelo.safacation.data.remote.MapsApiConfig
import com.sawelo.safacation.data.remote.MapsDetailsReviewResponse
import com.sawelo.safacation.data.remote.MapsFindPlacesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _idLokasi = MutableLiveData<String>()
    private val _namaLokasi = MutableLiveData<String>()
    private val _placeCoordinate = MutableLiveData<LatLng>()
    private val _isLoading = MutableLiveData<Boolean>()

    val idLokasi: LiveData<String> = _idLokasi
    val namaLokasi: LiveData<String> = _namaLokasi
    val placeCoordinate: LiveData<LatLng> = _placeCoordinate
    val isLoading: LiveData<Boolean> = _isLoading

    fun setNamaLokasi(namaLokasi: String) {
        _namaLokasi.value = namaLokasi
    }

    fun getReview(idLokasi: String?, result: (review: List<ReviewLokasi>) -> Unit) {
        _isLoading.value = true

        if (idLokasi != null) {
            val client = MapsApiConfig.getApiService().getDetails(idLokasi, "review", API_KEY)
            client.enqueue(object : Callback<MapsDetailsReviewResponse> {
                override fun onResponse(
                    call: Call<MapsDetailsReviewResponse>,
                    response: Response<MapsDetailsReviewResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val reviews = response.body()?.result?.reviews
                        val reviewLokasi = reviews?.map {
                            ReviewLokasi(it.authorName, it.text)
                        }
                        reviewLokasi?.let {result.invoke(it)}
                    }
                }

                override fun onFailure(call: Call<MapsDetailsReviewResponse>, t: Throwable) {
                    _isLoading.value = false
                }
            })
        }
    }

    fun findPlace(onFailure: (message: String) -> Unit) {
        _isLoading.value = true
        val nama = namaLokasi.value
        if (nama != null) {
            val client = MapsApiConfig.getApiService().findPlaceFromText(nama, "place_id,geometry", API_KEY)
            client.enqueue(object : Callback<MapsFindPlacesResponse> {
                override fun onResponse(
                    call: Call<MapsFindPlacesResponse>,
                    response: Response<MapsFindPlacesResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val candidates = response.body()?.candidates
                        if (!candidates.isNullOrEmpty()) {
                            println("EFUHEF $candidates")
                            val location = candidates[0].geometry.location
                            _placeCoordinate.value = LatLng(location.lat, location.lng)
                            _idLokasi.value = candidates[0].placeId
                        }
                    } else {
                        onFailure.invoke(response.message())
                    }
                }

                override fun onFailure(call: Call<MapsFindPlacesResponse>, t: Throwable) {
                    _isLoading.value = false
                    onFailure.invoke(t.message.toString())
                }
            })
        }
    }

    companion object {
        const val TAG = "DetailViewModel"
        const val API_KEY = BuildConfig.MAPS_API_KEY
    }
}
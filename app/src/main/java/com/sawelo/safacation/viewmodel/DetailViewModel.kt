package com.sawelo.safacation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.sawelo.safacation.BuildConfig
import com.sawelo.safacation.data.MapsApiConfig
import com.sawelo.safacation.data.MapsFindPlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class DetailViewModel : ViewModel() {

    private val _namaLokasi = MutableLiveData<String>()
    private val _placeCoordinate = MutableLiveData<LatLng>()
    private val _isLoading = MutableLiveData<Boolean>()

    val namaLokasi: LiveData<String> = _namaLokasi
    val placeCoordinate: LiveData<LatLng> = _placeCoordinate
    val isLoading: LiveData<Boolean> = _isLoading

    fun setNamaLokasi(namaLokasi: String) {
        _namaLokasi.value = namaLokasi
    }

    fun findPlace(onFailure: (message: String) -> Unit) {
        _isLoading.value = true

        val input = URLEncoder.encode(namaLokasi.value, "utf-8")
        val fields = URLEncoder.encode("geometry", "utf-8")

        val client = MapsApiConfig.getApiService().findPlaceFromText(input, fields, API_KEY)
        client.enqueue(object : Callback<MapsFindPlaceResponse> {
            override fun onResponse(
                call: Call<MapsFindPlaceResponse>,
                response: Response<MapsFindPlaceResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val candidates = response.body()?.candidates
                    if (candidates != null) {
                        val location = candidates[0].geometry.location
                        _placeCoordinate.value = LatLng(location.lat, location.lng)
                    }
                } else {
                    onFailure.invoke(response.message())
                }
            }

            override fun onFailure(call: Call<MapsFindPlaceResponse>, t: Throwable) {
                _isLoading.value = false
                onFailure.invoke(t.message.toString())
            }
        })
    }

    companion object {
        const val TAG = "DetailViewModel"
        const val API_KEY = BuildConfig.MAPS_API_KEY
    }
}
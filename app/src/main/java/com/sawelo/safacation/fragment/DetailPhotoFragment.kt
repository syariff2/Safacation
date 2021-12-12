package com.sawelo.safacation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.sawelo.safacation.R
import com.sawelo.safacation.adapter.DetailPhotoAdapter

class DetailPhotoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.apply {
            val imageView = view.findViewById<ImageView>(R.id.detail_photo_item)
            imageView.setImageResource(getInt(DetailPhotoAdapter.PHOTO_ARGS))
        }
    }
}
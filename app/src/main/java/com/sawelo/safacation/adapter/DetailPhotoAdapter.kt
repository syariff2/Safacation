package com.sawelo.safacation.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sawelo.safacation.fragment.DetailPhotoFragment

class DetailPhotoAdapter(
    fragmentManager: FragmentManager,
    lifeCycle: Lifecycle,
    private val photoList: List<Int>,
) :
    FragmentStateAdapter(fragmentManager, lifeCycle) {
    override fun getItemCount(): Int = ITEM_COUNT

    override fun createFragment(position: Int): Fragment {
        val fragment = DetailPhotoFragment()
        fragment.arguments = Bundle().apply {
            putInt(PHOTO_ARGS, photoList[position])
        }
        return fragment
    }

    companion object {
        const val PHOTO_ARGS = "PHOTO_ARGS"
        const val ITEM_COUNT = 3
    }
}
package com.sawelo.safacation

import android.os.Parcel
import android.os.Parcelable

class AdapterSafa() : RecyclerView.Adapter<AdapterSafa.ListViewHolder>(), Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    class ListViewHolder {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdapterSafa> {
        override fun createFromParcel(parcel: Parcel): AdapterSafa {
            return AdapterSafa(parcel)
        }

        override fun newArray(size: Int): Array<AdapterSafa?> {
            return arrayOfNulls(size)
        }
    }
}
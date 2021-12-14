package com.sawelo.safacation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sawelo.safacation.SafaApplication.Companion.databaseInstance
import com.sawelo.safacation.data.DataSafa

class MainViewModel: ViewModel() {
    private val dao = databaseInstance.DataSafaDao()
    val dataSafa: LiveData<List<DataSafa>> = dao.getAll()
}
package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel : ViewModel() {

    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    init {
        _total.value = 0
        _date.value = ""
    }

    fun incrementTotal(newDate: String) {
        _total.value = (_total.value ?: 0) + 1
        _date.value = newDate
    }

    fun setTotal(value: Int) {
        _total.value = value
    }

    fun setDate(value: String) {
        _date.value = value
    }
}

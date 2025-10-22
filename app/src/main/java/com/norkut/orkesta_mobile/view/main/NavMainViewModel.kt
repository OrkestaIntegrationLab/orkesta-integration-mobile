package com.norkut.orkesta_mobile.view.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavMainViewModel @Inject constructor() : ViewModel() {
    val menu: MutableList<Pair<Int, Int>> = mutableListOf()
}
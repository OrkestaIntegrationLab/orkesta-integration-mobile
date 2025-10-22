package com.norkut.orkesta_mobile.common.utils

import com.norkut.orkesta_mobile.R

data class FormState(
    val globalInfo: Int? = null,
    val dialogStr: Pair<String, String?> = Pair(String(), String()),
    val dialogRes: Pair<String, Int> = Pair(String(), R.string.empty_resource),
    val success: Int = R.string.empty_resource,
    val toast: Int = R.string.empty_resource,
    val fieldErrors: MutableMap<Int, Int> = mutableMapOf()
)

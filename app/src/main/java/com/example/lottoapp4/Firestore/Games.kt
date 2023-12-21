package com.example.lottoapp4.Firestore

data class Games(
    val id: String = "",
    val time: String = "",
    var email: String = "",
    var selNumb: List<Int>? = null,
    var drawNumb: List<Int>? = null,
    var win: Double = 0.0
)
package com.example.wagbat

data class HistoryData(
    val dishId: String = "",
    val dishName: String = "",
    val dishImage: String = "",
    val price: Double
) {
    // No-argument constructor
    constructor() : this("", "", "", 0.0)
}

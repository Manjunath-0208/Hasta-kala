package com.hastakala.app

object AppConstants {

    val PRODUCTS = listOf("Bag", "Keychain", "Wallet", "Scarf", "Cushion Cover")
    val COLOURS = listOf("Red", "Blue", "Green", "Yellow", "White", "Black")

    const val LOW_STOCK_THRESHOLD = 3

    // Colour hex values for charts matching SRD colour palette
    val CHART_COLOURS = intArrayOf(
        0xFFE53935.toInt(), // Red
        0xFF1E88E5.toInt(), // Blue
        0xFF43A047.toInt(), // Green
        0xFFFFC107.toInt(), // Yellow / Amber
        0xFF9E9E9E.toInt(), // White (grey for chart)
        0xFF212121.toInt()  // Black
    )

    enum class Period { THIS_WEEK, THIS_MONTH, ALL_TIME }
}

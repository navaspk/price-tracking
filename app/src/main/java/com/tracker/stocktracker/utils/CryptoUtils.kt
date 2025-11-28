package com.tracker.stocktracker.utils

import com.tracker.stocktracker.R

// 25 symbols
val symbols = listOf(
    "AAPL", "GOOG", "TSLA", "AMZN", "MSFT", "NVDA", "META", "INTC", "AMD", "ORCL",
    "NFLX", "ADBE", "CRM", "UBER", "LYFT", "BABA", "SPOT", "QCOM", "CSCO", "TXN",
    "SBUX", "NKE", "MCD", "WMT", "DIS"
)

/**
 * To manipulate each image for each asset
 */
fun getNameAndIconForCurrency(symbol: String): Pair<String, Int> {
    return when (symbol.uppercase()) {
        "AAPL" -> "Apple Inc" to R.drawable.ic_default_image
        "GOOG" -> "Alphabet Inc" to  R.drawable.ic_default_image
        "TSLA" -> "Tesla Inc" to R.drawable.ic_default_image
        "AMZN" -> "Amazon.com Inc" to R.drawable.ic_default_image
        "MSFT" -> "Microsoft Corp" to R.drawable.ic_default_image
        "NVDA" -> "NVIDIA Corp" to R.drawable.ic_default_image
        "META" -> "Meta Platforms Inc" to R.drawable.ic_default_image
        "INTC" -> "Intel Corp" to R.drawable.ic_default_image
        "AMD" -> "Advanced Micro Devices" to R.drawable.ic_default_image
        "ORCL" -> "Oracle Corp" to R.drawable.ic_default_image
        "NFLX" -> "Netflix Inc" to R.drawable.ic_default_image
        "ADBE" -> "Adobe Inc" to R.drawable.ic_default_image
        "CRM" -> "Salesforce Inc" to R.drawable.ic_default_image
        "UBER" -> "Uber Technologies" to R.drawable.ic_default_image
        "LYFT" -> "Lyft Inc" to R.drawable.ic_default_image
        "BABA" -> "Alibaba Group" to R.drawable.ic_default_image
        "SPOT" -> "Spotify Technology" to R.drawable.ic_default_image
        "QCOM" -> "Qualcomm Inc" to R.drawable.ic_default_image
        "CSCO" -> "Cisco Systems" to R.drawable.ic_default_image
        "TXN" -> "Texas Instruments" to R.drawable.ic_default_image
        "SBUX" -> "Starbucks Corp" to R.drawable.ic_default_image
        "NKE" -> "Nike Inc" to R.drawable.ic_default_image
        "MCD" -> "McDonald's Corp" to R.drawable.ic_default_image
        "WMT" -> "Walmart Inc" to R.drawable.ic_default_image
        "DIS" -> "Walt Disney Co" to R.drawable.ic_default_image
        else -> "Unknown" to R.drawable.ic_default_image
    }
}
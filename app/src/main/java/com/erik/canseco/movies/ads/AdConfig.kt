package com.erik.canseco.movies.ads

import com.erik.canseco.movies.BuildConfig

object AdConfig {
    val IS_TEST_MODE: Boolean = BuildConfig.DEBUG
    const val TEST_APP_ID: String = "ca-app-pub-3940256099942544~3347511713"
    const val TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"

    const val PROD_APP_ID: String = "ca-app-pub-3361811323965412~4086334562"
    const val PRODUCTION_BANNER_ID = "ca-app-pub-3361811323965412/2553761040"
    const val PRODUCTION_INTERSTITIAL_ID = "ca-app-pub-3361811323965412/7677387376"

    val appId: String
        get() = if (IS_TEST_MODE) TEST_APP_ID else PROD_APP_ID

    fun isValidAdUnitId(adUnitId: String?): Boolean {
        if (adUnitId.isNullOrBlank()) return false
        return adUnitId.startsWith("ca-app-pub-") && adUnitId.contains("/") && !adUnitId.contains("~")
    }

    private fun getSafeAdUnitId(prodId: String, testId: String): String {
        return if (IS_TEST_MODE || !isValidAdUnitId(prodId)) {
            testId
        } else {
            prodId
        }
    }
    val bannerAdUnitId: String
        get() = getSafeAdUnitId(PRODUCTION_BANNER_ID, TEST_BANNER_ID)

    val interstitialAdUnitId: String
        get() = getSafeAdUnitId(PRODUCTION_INTERSTITIAL_ID,TEST_INTERSTITIAL_ID)
}

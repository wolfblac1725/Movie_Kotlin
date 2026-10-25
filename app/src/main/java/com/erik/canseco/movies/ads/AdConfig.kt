package com.erik.canseco.movies.ads

import com.erik.canseco.movies.BuildConfig

object AdConfig {
    const val TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"

    const val PRODUCTION_BANNER_ID = "ca-app-pub-3361811323965412/2553761040"
    const val PRODUCTION_INTERSTITIAL_ID = "ca-app-pub-3361811323965412/7677387376"

    val bannerAdUnitId: String
        get() = if (BuildConfig.DEBUG) TEST_BANNER_ID else PRODUCTION_BANNER_ID

    val interstitialAdUnitId: String
        get() = if (BuildConfig.DEBUG) TEST_INTERSTITIAL_ID else PRODUCTION_INTERSTITIAL_ID
}

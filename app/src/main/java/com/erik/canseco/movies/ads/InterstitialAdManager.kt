package com.erik.canseco.movies.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object InterstitialAdManager {
    private const val PREFS_NAME = "ad_frequency"
    private const val KEY_ACTIONS_SINCE_LAST_AD = "actions_since_last_ad"
    private const val KEY_LAST_SHOWN_AT = "last_shown_at"
    private const val REQUIRED_ACTIONS = 3
    private const val MIN_INTERVAL_MILLIS = 10 * 60 * 1000L

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false

    fun preload(context: Context) {
        val adUnitId = AdConfig.interstitialAdUnitId
        if (!AdsConsentManager.canRequestAds.value || adUnitId.isBlank() || isLoading || interstitialAd != null) return

        isLoading = true
        InterstitialAd.load(
            context.applicationContext,
            adUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                    Log.w("InterstitialAdManager", "Interstitial failed to load: ${error.message}")
                }
            }
        )
    }

    fun onQualifyingAction(activity: Activity) {
        if (!AdsConsentManager.canRequestAds.value) return

        val preferences = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val actions = preferences.getInt(KEY_ACTIONS_SINCE_LAST_AD, 0) + 1
        preferences.edit().putInt(KEY_ACTIONS_SINCE_LAST_AD, actions).apply()

        val lastShownAt = preferences.getLong(KEY_LAST_SHOWN_AT, 0L)
        val intervalElapsed = System.currentTimeMillis() - lastShownAt >= MIN_INTERVAL_MILLIS
        val ad = interstitialAd
        if (actions < REQUIRED_ACTIONS || !intervalElapsed || ad == null) {
            preload(activity)
            return
        }

        interstitialAd = null
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                recordImpressionTime(preferences)
            }

            override fun onAdDismissedFullScreenContent() {
                preload(activity)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                interstitialAd = null
                Log.w("InterstitialAdManager", "Interstitial failed to show: ${error.message}")
                preload(activity)
            }
        }
        ad.show(activity)
    }

    private fun recordImpressionTime(preferences: android.content.SharedPreferences) {
        preferences.edit()
            .putLong(KEY_LAST_SHOWN_AT, System.currentTimeMillis())
            .putInt(KEY_ACTIONS_SINCE_LAST_AD, 0)
            .apply()
    }
}

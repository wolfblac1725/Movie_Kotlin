package com.erik.canseco.movies

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.erik.canseco.movies.ads.AdsConsentManager
import com.erik.canseco.movies.ads.InterstitialAdManager
import com.erik.canseco.movies.navigation.NavRoot
import com.erik.canseco.movies.ui.theme.MoviesTheme
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesTheme {
                val navController = rememberNavController()
                NavRoot(navController = navController)
            }
        }
        requestAdsConsent()
    }

    private fun requestAdsConsent() {
        val consentInformation = UserMessagingPlatform.getConsentInformation(this)
        val parameters = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()
        consentInformation.requestConsentInfoUpdate(
            this,
            parameters,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(this) { formError ->
                    formError?.let {
                        Log.w("MovieApp", "Consent form error: ${it.message}")
                    }
                    updateAdsAvailability(consentInformation.canRequestAds())
                }
            },
            { requestError ->
                Log.w("MovieApp", "Consent information error: ${requestError.message}")
                updateAdsAvailability(consentInformation.canRequestAds())
            }
        )
    }

    private fun updateAdsAvailability(canRequestAds: Boolean) {
        val consentInformation = UserMessagingPlatform.getConsentInformation(this)
        val privacyOptionsRequired =
            consentInformation.privacyOptionsRequirementStatus ==
                com.google.android.ump.ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
        AdsConsentManager.updateConsent(canRequestAds, privacyOptionsRequired)
        if (canRequestAds && AdsConsentManager.markSdkInitialized()) {
            MobileAds.initialize(this) {
                InterstitialAdManager.preload(this)
            }
        }
    }
}
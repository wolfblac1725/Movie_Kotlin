package com.erik.canseco.movies.ads

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AdsConsentManager {
    private var sdkInitialized = false

    private val _canRequestAds = MutableStateFlow(false)
    val canRequestAds = _canRequestAds.asStateFlow()

    private val _privacyOptionsRequired = MutableStateFlow(false)
    val privacyOptionsRequired = _privacyOptionsRequired.asStateFlow()

    fun updateConsent(canRequestAds: Boolean, privacyOptionsRequired: Boolean) {
        _canRequestAds.value = canRequestAds
        _privacyOptionsRequired.value = privacyOptionsRequired
    }

    fun markSdkInitialized(): Boolean {
        if (sdkInitialized) return false
        sdkInitialized = true
        return true
    }
}

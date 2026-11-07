package com.erik.canseco.movies.presentation.screen.home

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.erik.canseco.movies.ads.AdMobBanner
import com.erik.canseco.movies.ads.AdsConsentManager
import com.erik.canseco.movies.R
import com.erik.canseco.movies.ads.InterstitialAdManager
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.presentation.component.BottomNavigationBar
import com.erik.canseco.movies.presentation.component.ListViewComponent
import com.erik.canseco.movies.utility.TypeShared
import com.google.android.ump.UserMessagingPlatform
import com.google.android.gms.ads.MobileAds

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current
    val privacyOptionsRequired by AdsConsentManager.privacyOptionsRequired.collectAsState()
    val state = homeViewModel.state

    HomeScreen(
        modifier = modifier,
        state = state,
        movies = homeViewModel.moviesFor(state.selectedCategory).collectAsLazyPagingItems(),
        privacyOptionsRequired = privacyOptionsRequired,
        onOpenPrivacyOptions = {
            (context as? Activity)?.let { activity ->
                UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
                    formError?.let {
                        Log.w("MovieApp", "Privacy options form error: ${it.message}")
                    }
                    val consentInformation =
                        UserMessagingPlatform.getConsentInformation(activity)
                    val canRequestAds = consentInformation.canRequestAds()
                    AdsConsentManager.updateConsent(
                        canRequestAds,
                        consentInformation.privacyOptionsRequirementStatus ==
                            com.google.android.ump.ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
                    )
                    if (canRequestAds && AdsConsentManager.markSdkInitialized()) {
                        MobileAds.initialize(activity) {
                            InterstitialAdManager.preload(activity)
                        }
                    }
                }
            }
        },
        action = { action ->
            when(action) {
                is ActionHome.DetailMovie -> {
                    onNavigate(action.movie)
                }
                else ->{
                    homeViewModel.onAction(action)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    modifier: Modifier = Modifier,
    movies: LazyPagingItems<Movie>,
    privacyOptionsRequired: Boolean,
    onOpenPrivacyOptions: () -> Unit,
    action: (ActionHome) -> Unit
) {
    val listStateHolder = rememberSaveableStateHolder()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Movie")
                },
                actions = {
                    if (privacyOptionsRequired) {
                        TextButton(onClick = onOpenPrivacyOptions) {
                            Text(stringResource(R.string.privacy_options))
                        }
                    }
                }
            )
        },
        content = { padding ->
            listStateHolder.SaveableStateProvider(
                key = when (state.selectedCategory) {
                    TypeShared.Popular -> "popular"
                    TypeShared.TopRated -> "top_rated"
                    TypeShared.NowPlaying -> "now_playing"
                }
            ) {
                ListViewComponent(
                    column = 2,
                    movies = movies,
                    action = action,
                    contentPadding = padding
                )
            }
        },
        bottomBar = {
            Column {
                BottomNavigationBar(
                    selectedCategory = state.selectedCategory,
                    onEvent = action
                )
                AdMobBanner()
            }
        }
    )
}